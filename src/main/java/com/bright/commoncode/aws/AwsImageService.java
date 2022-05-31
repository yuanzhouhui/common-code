package com.bright.commoncode.aws;

import cn.hutool.core.io.FileTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * @author yuanzhouhui
 * @description
 * @date 2022-05-31 17:29
 */
@Slf4j
@Service
public class AwsImageService {

	@Autowired
	private AwsCloudStorageService awsCloudStorageService;

	@Value("${file.path-prefix:/}")
	private String pathPrefix;

	@Value("${file.nginx-path-prefix:http://capsulemining.test.s3.ap-east-1.amazonaws.com}")
	private String nginxUrlPrefix;

	/**
	 * 图片上传到亚马逊服务器和返回一个url
	 *
	 * @param inputStream
	 * @return
	 */
	public String creatAndUploadImage(InputStream inputStream) {
		try {
			byte[] byteArray = Objects.requireNonNull(cloneInputStream(inputStream)).toByteArray();
			String type = FileTypeUtil.getType(new ByteArrayInputStream(byteArray));
			log.info("文件类型：" + type);
			String fileMd5 = UUID.randomUUID().toString().replace("-", "");
			StringBuilder floderStr = new StringBuilder();
			floderStr.append(pathPrefix.endsWith("/") ? pathPrefix : pathPrefix.concat("/"))
					.append(LocalDate.now().toString())
					.append("/");
			String pathname = floderStr.toString().concat(fileMd5).concat(".").concat(type);
			ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
			String urlStr;
			String urlPath = awsCloudStorageService.upload(arrayInputStream, pathname, Long.valueOf(arrayInputStream.available()));
			urlStr = getPathByURLString(urlPath);
			return nginxUrlPrefix.concat(urlStr);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	private String getPathByURLString(String urlStr) {
		log.info("urlStr 入参=[{}]", urlStr);
		try {
			URL url = new URL(urlStr);
			return url.getPath();
		} catch (MalformedURLException e) {
			log.error("MalformedURLException:", e);
		}
		return StringUtils.EMPTY;
	}

	/**
	 * 输入流复制
	 */
	private ByteArrayOutputStream cloneInputStream(InputStream input) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[8192];
			int len;
			while ((len = input.read(buffer)) > -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			input.close();
			return baos;
		} catch (IOException e) {
			log.error("IOException e:", e);
			return null;
		}
	}
}
