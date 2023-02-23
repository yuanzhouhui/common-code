package com.bright.commoncode.excel.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.exception.ExcelRuntimeException;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.bright.commoncode.excel.convert.ExcelBigNumberConvert;
import com.bright.commoncode.excel.core.CellMergeStrategy;
import com.bright.commoncode.excel.core.DefaultExcelListener;
import com.bright.commoncode.excel.core.ExcelListener;
import com.bright.commoncode.excel.core.ExcelResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ExcelUtil
 *
 * @author YuanZhouhui
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelUtil {

    private static final String ERROR_MSG = "导出Excel异常";

    /**
     * 同步导入(适用于小数据量)
     *
     * @param is 输入流
     * @return 转换后集合
     */
    public static <T> List<T> importExcel(InputStream is, Class<T> clazz) {
        return EasyExcelFactory.read(is).head(clazz).autoCloseStream(false).sheet().doReadSync();
    }


    /**
     * 使用校验监听器 异步导入 同步返回
     *
     * @param is         输入流
     * @param clazz      对象类型
     * @param isValidate 是否 Validator 检验 默认为是
     * @return 转换后集合
     */
    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, boolean isValidate) {
        DefaultExcelListener<T> listener = new DefaultExcelListener<>(isValidate);
        EasyExcelFactory.read(is, clazz, listener).sheet().doRead();
        return listener.getExcelResult();
    }

    /**
     * 使用校验监听器 异步导入 同步返回
     *
     * @param is            输入流
     * @param clazz         对象类型
     * @param isValidate    是否 Validator 检验 默认为是
     * @param headRowNumber 开始读取行数
     * @return 转换后集合
     */
    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, boolean isValidate, int headRowNumber) {
        DefaultExcelListener<T> listener = new DefaultExcelListener<>(isValidate);
        EasyExcelFactory.read(is, clazz, listener).headRowNumber(headRowNumber).sheet().doRead();
        return listener.getExcelResult();
    }

    /**
     * 使用自定义监听器 异步导入 自定义返回
     *
     * @param is       输入流
     * @param clazz    对象类型
     * @param listener 自定义监听器
     * @return 转换后集合
     */
    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, int headRowNumber, ExcelListener<T> listener) {
        EasyExcelFactory.read(is, clazz, listener).headRowNumber(headRowNumber).sheet().doRead();
        return listener.getExcelResult();
    }

    /**
     * 使用自定义监听器 异步导入 自定义返回
     *
     * @param is       输入流
     * @param clazz    对象类型
     * @param listener 自定义监听器
     * @return 转换后集合
     */
    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, int headRowNumber, String sheetName, ExcelListener<T> listener) {
        EasyExcelFactory.read(is, clazz, listener).headRowNumber(headRowNumber).sheet(sheetName).doRead();
        return listener.getExcelResult();
    }

    /**
     * 使用自定义监听器 异步导入 自定义返回
     *
     * @param is       输入流
     * @param clazz    对象类型
     * @param listener 自定义监听器
     * @return 转换后集合
     */
    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, ExcelListener<T> listener) {
        EasyExcelFactory.read(is, clazz, listener).sheet().doRead();
        return listener.getExcelResult();
    }

    /**
     * 导出excel
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @param clazz     实体类
     * @param response  响应体
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, HttpServletResponse response) {
        exportExcel(list, sheetName, clazz, false, response);
    }

    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, Set<String> excludeColumnFiledNames, HttpServletResponse response) {
        exportExcel(list, sheetName, clazz, excludeColumnFiledNames, false, response);
    }

    /**
     * 导出excel
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @param clazz     实体类
     * @param merge     是否合并单元格
     * @param response  响应体
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, boolean merge, HttpServletResponse response) {
        try {
            resetResponse(sheetName, response);
            ServletOutputStream os = response.getOutputStream();
            ExcelWriterSheetBuilder builder = EasyExcelFactory.write(os, clazz)
                    .autoCloseStream(false)
                    // 自动适配
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .registerWriteHandler(setCellCenterType())
                    // 大数值自动转换 防止失真
                    .registerConverter(new ExcelBigNumberConvert())
                    .sheet(sheetName);
            if (merge) {
                // 合并处理器
                builder.registerWriteHandler(new CellMergeStrategy(list, true));
            }
            builder.doWrite(list);
        } catch (IOException e) {
            throw new ExcelRuntimeException(ERROR_MSG);
        }
    }

    /**
     * 导出excel
     *
     * @param list                    导出数据集合
     * @param sheetName               工作表的名称
     * @param clazz                   实体类
     * @param excludeColumnFiledNames 忽略的字段
     * @param merge                   是否合并单元格
     * @param response                响应体
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, Set<String> excludeColumnFiledNames, boolean merge, HttpServletResponse response) {
        try {
            resetResponse(sheetName, response);
            ServletOutputStream os = response.getOutputStream();
            ExcelWriterSheetBuilder builder = EasyExcelFactory.write(os, clazz)
                    .autoCloseStream(false)
                    // 自动适配
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .registerWriteHandler(setCellCenterType())
                    // 大数值自动转换 防止失真
                    .registerConverter(new ExcelBigNumberConvert())
                    //指定忽略的字段
                    .excludeColumnFieldNames(excludeColumnFiledNames)
                    .sheet(sheetName);
            if (merge) {
                // 合并处理器
                builder.registerWriteHandler(new CellMergeStrategy(list, true));
            }
            builder.doWrite(list);
        } catch (IOException e) {
            throw new ExcelRuntimeException(ERROR_MSG);
        }
    }

    /**
     * 单表多数据模板导出 模板格式为 {.属性}
     *
     * @param filename     文件名
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     */
    public static void exportTemplate(List<Object> data, String filename, String templatePath, HttpServletResponse response) {
        try {
            resetResponse(filename, response);
            ClassPathResource templateResource = new ClassPathResource(templatePath);
            ExcelWriter excelWriter = EasyExcelFactory.write(response.getOutputStream())
                    .withTemplate(templateResource.getStream())
                    .autoCloseStream(false)
                    // 大数值自动转换 防止失真
                    .registerConverter(new ExcelBigNumberConvert())
                    .build();
            WriteSheet writeSheet = EasyExcelFactory.writerSheet().build();
            if (CollUtil.isEmpty(data)) {
                throw new IllegalArgumentException("数据为空");
            }
            // 单表多数据导出 模板格式为 {.属性}
            for (Object d : data) {
                excelWriter.fill(d, writeSheet);
            }
            excelWriter.finish();
        } catch (IOException e) {
            throw new ExcelRuntimeException(ERROR_MSG);
        }
    }

    /**
     * 多表多数据模板导出 模板格式为 {key.属性}
     *
     * @param filename     文件名
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     */
    public static void exportTemplateMultiList(Map<String, Object> data, String filename, String templatePath, HttpServletResponse response) {
        try {
            resetResponse(filename, response);
            ClassPathResource templateResource = new ClassPathResource(templatePath);
            ExcelWriter excelWriter = EasyExcelFactory.write(response.getOutputStream())
                    .withTemplate(templateResource.getStream())
                    .autoCloseStream(false)
                    // 大数值自动转换 防止失真
                    .registerConverter(new ExcelBigNumberConvert())
                    .build();
            WriteSheet writeSheet = EasyExcelFactory.writerSheet().build();
            if (CollUtil.isEmpty(data)) {
                throw new IllegalArgumentException("数据为空");
            }
            for (Map.Entry<String, Object> map : data.entrySet()) {
                // 设置列表后续还有数据
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
                if (map.getValue() instanceof Collection) {
                    // 多表导出必须使用 FillWrapper
                    excelWriter.fill(new FillWrapper(map.getKey(), (Collection<?>) map.getValue()), fillConfig, writeSheet);
                } else {
                    excelWriter.fill(map.getValue(), writeSheet);
                }
            }
            excelWriter.finish();
        } catch (IOException e) {
            throw new ExcelRuntimeException(ERROR_MSG);
        }
    }

    /**
     * 重置响应体
     */
    private static void resetResponse(String sheetName, HttpServletResponse response) throws UnsupportedEncodingException {
        String filename = encodingFilename(sheetName);
        response.reset();
        setAttachmentResponseHeader(response, filename);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
    }

    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException {
        String percentEncodedFileName = percentEncode(realFileName);

        String contentDispositionValue = "attachment; filename=" +
                percentEncodedFileName +
                ";" +
                "filename*=" +
                "utf-8''" +
                percentEncodedFileName;

        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue);
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return StringUtils.replace(encode, "\\+", "%20");
    }

    /**
     * 解析导出值 0=男,1=女,2=未知
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @param separator     分隔符
     * @return 解析后值
     */
    public static String convertByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(propertyValue, separator)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[0].equals(value)) {
                        propertyString.append(itemArray[1]).append(separator);
                        break;
                    }
                }
            } else {
                if (itemArray[0].equals(propertyValue)) {
                    return itemArray[1];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 反向解析值 男=0,女=1,未知=2
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @param separator     分隔符
     * @return 解析后值
     */
    public static String reverseByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(propertyValue, separator)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[1].equals(value)) {
                        propertyString.append(itemArray[0]).append(separator);
                        break;
                    }
                }
            } else {
                if (itemArray[1].equals(propertyValue)) {
                    return itemArray[0];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 编码文件名
     */
    public static String encodingFilename(String filename) {
        return filename + ".xlsx";
    }

    public static HorizontalCellStyleStrategy setCellCenterType() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }
}
