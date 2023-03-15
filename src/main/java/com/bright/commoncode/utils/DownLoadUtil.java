package com.bright.commoncode.utils;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.bright.commoncode.enums.ErrorCodeEnum;
import com.bright.commoncode.exception.CoreException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author yuanzhouhui
 * @date 2023-03-15 15:02
 * @description 批量下载
 */
public class DownLoadUtil {

    private DownLoadUtil() {
    }

    /**
     * 分文件夹压缩下载
     * 参数
     */
    public static void bulkDownload() {
        HttpServletResponse response = ServletUtils.getResponse();
        String fileName = "文档批量下载";
        //创建根目录
        File rootFile = new File(fileName);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        //TODO 根据实际情况,创建次级目录
        String secondLevelName = "次级目录";
        File secondLevelFile = new File(rootFile, secondLevelName);
        if (!secondLevelFile.exists()) {
            secondLevelFile.mkdirs();
        }
        //TODO 实例文件,根据实际情况调整
        try {
            //TODO 实例文件名,根据实际情况调整
            String name = "name";
            //获取文件流
            InputStream is = getFileInputStream(name);
            //获取文件二进制数据
            byte[] bt = readInputStream(is);
            //创建文件
            File file = new File(secondLevelFile, name);
            //写文件
            writeFile(bt, file);
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName + System.currentTimeMillis() / 1000 + ".zip", "UTF-8"));
            OutputStream fos = response.getOutputStream();
            //压缩
            zipFile(rootFile, fos);
            //删除文件夹
            delFolder(fileName);
        } catch (Exception e) {
            throw new CoreException(ErrorCodeEnum.FILE_READ_FAIL);
        }
    }

    private static InputStream getFileInputStream(String name) {
        //TODO 实例路径,根据实际情况调整
        String ssoUrl = "172.27.10.240";
        String url = UrlBuilder.ofHttp(ssoUrl)
                .addPath("/fastDfs/download")
                .addQuery("fileId", name).build();
        HttpRequest httpRequest = HttpRequest.get(url);
        HttpResponse execute = httpRequest.execute();
        return execute.bodyStream();
    }

    private static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    private static void writeFile(byte[] bt, File file) {
        try (FileOutputStream outStream = new FileOutputStream(file)) {
            outStream.write(bt);
        } catch (IOException e) {
            throw new CoreException(ErrorCodeEnum.FILE_READ_FAIL);
        }
    }

    private static void zipFile(File rootFile, OutputStream fos) {
        try (ZipOutputStream zos = new ZipOutputStream(fos)) {
            //压缩
            compress(rootFile, zos, rootFile.getName(), true);
        } catch (IOException e) {
            throw new CoreException(ErrorCodeEnum.FILE_READ_FAIL);
        }
    }

    private static void compress(File sourceFile, ZipOutputStream zos, String fileName, boolean keepDirStructure) throws IOException {
        byte[] buf = new byte[2 * 1024];
        // 判断是否是一个文件
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(fileName));
            // 创建文件（即某张图片）的输入流
            try (FileInputStream in = new FileInputStream(sourceFile)) {
                int len;
                // read方法：每调用一次就从FileInputStream流中读取一个字节，并返回下一个数据字节，若已到达末尾，就返回-1。
                while ((len = in.read(buf, 0, buf.length)) != -1) {
                    zos.write(buf, 0, len);
                }
                // 实际写入到了zip输出流的zip实体中，还没写到文件中【zip文件时0kb，不能打开，因为流没有关闭】
                zos.closeEntry();
            } catch (Exception e) {
                throw new CoreException(ErrorCodeEnum.FILE_READ_FAIL);
            }
        } else {
            // 源文件时目录
            // 获取该目录下所有文件和目录的绝对路径
            File[] listFiles = sourceFile.listFiles();
            // 空目录
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (keepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(fileName + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                // 非空目录
                for (File file : listFiles) {
                    if (keepDirStructure) {
                        // 注意：getName()仅得到最后一层的名字，不是路径，所以要加“/”，不然所有文件都跑到压缩包根目录下了
                        compress(file, zos, fileName + "/" + file.getName(), true);
                    } else {
                        compress(file, zos, file.getName(), false);
                    }
                }
            }
        }
    }

    public static void delFolder(String folderPath) {
        try {
            // 删除目录下所有内容
            delAllFile(folderPath);
            File myFilePath = new File(folderPath);
            //删除空文件夹
            myFilePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp;
        if (tempList != null) {
            for (String str : tempList) {
                // separator 代替文件或文件夹路径的斜线或反斜线，防止跨平台出现错误
                if (path.endsWith(File.separator)) {
                    temp = new File(path + str);
                } else {
                    temp = new File(path + File.separator + str);
                }
                if (temp.isFile()) {
                    temp.delete();
                }
                if (temp.isDirectory()) {
                    //先删除文件夹里面的文件
                    delAllFile(path + "/" + str);
                    //再删除空文件夹
                    delFolder(path + "/" + str);
                }
            }
        }
    }
}
