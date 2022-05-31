package com.bright.commoncode.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author yuanzhouhui
 * @description
 * @date 2022-05-31 16:42
 */
@Slf4j
public class FileUtils {

	/**
	 * 写文件到指定目录
	 *
	 * @param path 目录
	 * @param data 文件内容
	 */
	public static void writeFile(String path, JSONObject data) {
		File file = new File(path);
		//获取父目录
		File fileParent = file.getParentFile();
		//判断是否存在，如果不存在则创建
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		try (FileOutputStream fos = new FileOutputStream(path)) {
			BufferedOutputStream out = new BufferedOutputStream(fos);
			out.write(data.toJSONString().getBytes());
			out.flush();
		} catch (IOException e) {
			log.error("保存文件错误:{}", e.getMessage());
		}
	}
}
