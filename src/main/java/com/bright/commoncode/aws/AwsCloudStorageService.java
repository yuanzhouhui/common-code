package com.bright.commoncode.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author yuanzhouhui
 * @description
 * @date 2022-05-31 17:23
 */
@Slf4j
@Service
public class AwsCloudStorageService extends CloudStorageService {

	/**
	 * 代理
	 */
	@Value("${proxy.base.url:#{null}}")
	private String proxyUrl;
	@Value("${proxy.base.port:3129}")
	private int proxyPort;

	/**
	 * 亚马逊云路径前缀
	 */
	@Value("${aws.awsPrefix:#{null}}")
	private String awsPrefix;
	/**
	 * 亚马逊云AccessKeyId
	 */
	@Value("${aws.awsAccessKeyId:#{null}}")
	private String awsAccessKeyId;
	/**
	 * 亚马逊云SecretAccessKey
	 */
	@Value("${aws.awsSecretAccessKey:#{null}}")
	private String awsSecretAccessKey;
	/**
	 * 亚马逊云BucketName
	 */
	@Value("${aws.awsBucketName:#{null}}")
	private String awsBucketName;
	/**
	 * 亚马逊云所属地区
	 */
	@Value("${aws.awsRegion:#{null}}")
	private String awsRegion = "ap-northeast-1";

	@Value("${aws.enableProxy:true}")
	private String enableProxy;

	private AmazonS3 s3;

	URL s3Url;

	private void init() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
		ClientConfiguration clientConfig = new ClientConfiguration();
		//设置
		clientConfig.setConnectionTimeout(1000 * 60 * 2);
		clientConfig.setSocketTimeout(1000 * 60 * 20);
		if ("true".equalsIgnoreCase(enableProxy)) {
			clientConfig.setProxyHost(proxyUrl);
			clientConfig.setProxyPort(proxyPort);
			clientConfig.setProtocol(Protocol.HTTP);
		}

		s3 = AmazonS3ClientBuilder.standard()
				.withClientConfiguration(clientConfig)
				.withRegion(Regions.fromName(awsRegion))
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.build();

		//生成指定前缀的url
		s3Url = s3.generatePresignedUrl(new GeneratePresignedUrlRequest(
				awsBucketName, awsPrefix));
	}

	@Override
	public String upload(byte[] data, String path, Long contentLength) {
		return null;
	}

	@Override
	public String upload(InputStream inputStream, String path, Long contentLength) {
		try {
			return uploadToS3(inputStream, path, contentLength);
		} catch (Exception biz) {
			log.error("上传文件失败", biz);
			return "";
		}
	}

	@Override
	public String upload(byte[] data, String endfix) {
		Long datalength = Long.parseLong(data.length + "");
		return upload(data, getPath(awsPrefix, endfix), datalength);
	}

	@Override
	public String upload(InputStream inputStream, Long contentLength, String endfix) {
		return upload(inputStream, getPath(awsPrefix, endfix), contentLength);
	}

	private String uploadToS3(InputStream inputStream, String path, Long contentLength) throws IOException {
		if (s3 == null) {
			init();
		}
		//设置bucket
		String bucketName = awsBucketName;
		S3Object object = null;
		try {
			//上传文件
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(contentLength);

			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, inputStream, metadata);

			s3.putObject(putObjectRequest);
			object = s3.getObject(new GetObjectRequest(bucketName, path));
			//获取一个request
			GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
					bucketName, path);
			//生成公用的url
			URL url = s3.generatePresignedUrl(urlRequest);
			if (url == null) {
				throw new IOException("上传失败");
			}
			String publicUrl = url.getProtocol() + "://" + url.getHost() + url.getPath();
			log.warn("upload上传成功，返回URL为：" + publicUrl);
			return publicUrl;
		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
			log.error("上传失败", ase);
			throw ase;
		} finally {
			if (object != null) {
				object.close();
			}
		}
	}

	@Override
	public String getPrefix() {
		return awsPrefix;
	}
}
