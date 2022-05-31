package com.bright.commoncode.aws;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author yuanzhouhui
 * @description
 * @date 2022-05-31 17:20
 */
public abstract class CloudStorageService {
	public static String format(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		}
		return null;
	}

	/**
	 * 文件路径
	 *
	 * @param prefix 前缀
	 * @return 返回上传路径
	 */
	public String getPath(String prefix, String endfix) {
		//生成uuid
		String uuid = UUID.randomUUID().toString().replace("-", "");
		//文件路径
		String path = String.format("%s/%s", format(new Date(), "yyyyMMdd"), uuid);

		if (StringUtils.isNotBlank(prefix)) {
			path = String.format("%s/%s", prefix, path);
		}
		if (StringUtils.isNotBlank(endfix)) {
			path = path + "." + endfix;
		}
		return path;
	}

	/**
	 * @return 返回前缀
	 */
	public abstract String getPrefix();

	/**
	 * 文件上传
	 *
	 * @param data 文件字节数组
	 * @param path 文件路径，包含文件名
	 * @return 返回http地址
	 */
	public abstract String upload(byte[] data, String path, Long contentLength);

	/**
	 * 文件上传
	 *
	 * @param data 文件字节数组
	 * @return 返回http地址
	 */
	public abstract String upload(byte[] data, String endfix);

	/**
	 * 文件上传
	 *
	 * @param inputStream 字节流
	 * @param path        文件路径，包含文件名
	 * @return 返回http地址
	 */
	public abstract String upload(InputStream inputStream, String path, Long contentLength);

	/**
	 * 文件上传
	 *
	 * @param inputStream 字节流
	 * @return 返回http地址
	 */
	public abstract String upload(InputStream inputStream, Long contentLength, String endfix);
}
