package com.bright.commoncode.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author yuanzhouhui
 * @description
 * @date 2022-05-31 18:06
 */
public class ExprtExcelUtil {

	/**
	 * 下载excel
	 *
	 * @param response
	 */
	public void export(HttpServletResponse response) {
		ExcelWriter writer = ExcelUtil.getWriter(true);
		String filename = URLEncoder.encode("数据导出", StandardCharsets.UTF_8);
		response.setContentType(ExcelUtil.XLSX_CONTENT_TYPE + "charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename + DateUtil.today() + ".xlsx");
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();

			//命名sheet1
			writer.renameSheet(0, "新用户");
			//写入sheet1
			writer.setSheet("新用户");
			//自定义标题别名
			writer.addHeaderAlias("userName", "用户名");
			writer.addHeaderAlias("nickName", "昵称");
			// 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
			writer.setOnlyAlias(true);
			//写出内容
			//writer.write();
			//设置自动宽度
			writer.autoSizeColumnAll();

			writer.flush(out, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
		IoUtil.close(out);
	}
}
