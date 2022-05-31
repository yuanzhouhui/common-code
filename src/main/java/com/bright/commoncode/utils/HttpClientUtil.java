package com.bright.commoncode.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yuanzhouhui
 * @description
 * @date 2022-05-31 17:48
 */
public class HttpClientUtil {

	public static final ExecutorService executor = Executors.newFixedThreadPool(5);

	public static final String APPLICATION_JSON = "application/json";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String FORM_URLENCODED = "application/x-www-form-urlencoded";

	public static HttpClient getClient() {

		//创建 builder
		HttpClient.Builder builder = HttpClient.newBuilder();
		//链式调用
		return builder
				//http 协议版本  1.1 或者 2
				.version(HttpClient.Version.HTTP_2)
				//.version(HttpClient.Version.HTTP_1_1)
				//连接超时时间，单位为毫秒
				.connectTimeout(Duration.ofMillis(5000))
				//.connectTimeout(Duration.ofMinutes(1))
				//连接完成之后的转发策略
				.followRedirects(HttpClient.Redirect.NEVER)
				//.followRedirects(HttpClient.Redirect.ALWAYS)
				//指定线程池
				.executor(executor)
				//认证，默认情况下 Authenticator.getDefault() 是 null 值，会报错
				//.authenticator(Authenticator.getDefault())
				//缓存，默认情况下 CookieHandler.getDefault() 是 null 值，会报错
				//.cookieHandler(CookieHandler.getDefault())
				//创建完成
				.build();
	}


	/**
	 * http get请求
	 *
	 * @param url
	 * @return
	 */
	public static String get(String url, Map<String, String> map) {
		//链式调用
		HttpClient client = getClient();
		//创建 builder
		HttpRequest.Builder reBuilder = HttpRequest.newBuilder();
		if (map != null) {
			Set<String> strings = map.keySet();
			for (String s : strings) {
				reBuilder.header(s, map.get(s));
			}
		}

		//链式调用
		HttpRequest request = reBuilder
				//存入消息头
				//消息头是保存在一张 TreeMap 里的
				.header(CONTENT_TYPE, APPLICATION_JSON)
				//http 协议版本
				.version(HttpClient.Version.HTTP_2)
				//url 地址
				.uri(URI.create(url))
				//超时时间
				.timeout(Duration.ofMillis(100000L))
				//发起一个 get 消息，get 不需要消息体
				.GET()
				.build();

		HttpResponse<String> response = null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return response.body();
	}

	/**
	 * http get请求 带超时时间参数
	 *
	 * @param url
	 * @param time 超时时间，单位为秒
	 * @return
	 */
	public static String get(String url, Map<String, String> map, Integer time) {
		//链式调用
		HttpClient client = getClient();
		//创建 builder
		HttpRequest.Builder reBuilder = HttpRequest.newBuilder();
		if (map != null) {
			Set<String> strings = map.keySet();
			for (String s : strings) {
				reBuilder.header(s, map.get(s));
			}
		}

		//链式调用
		HttpRequest request = reBuilder
				//存入消息头
				//消息头是保存在一张 TreeMap 里的
				.header(CONTENT_TYPE, APPLICATION_JSON)
				//http 协议版本
				.version(HttpClient.Version.HTTP_2)
				//url 地址
				.uri(URI.create(url))
				//超时时间
				.timeout(Duration.ofMillis(1000L * time))
				//发起一个 get 消息，get 不需要消息体
				.GET()
				.build();

		HttpResponse<String> response = null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException  e) {
			e.printStackTrace();
		}
		return response.body();
	}

	/**
	 * http get请求
	 *
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		return get(url, null);
	}

	/**
	 * json post请求
	 *
	 * @param url
	 * @param json
	 * @return
	 */
	public static String postJson(String url, String json) {
		return post(url, json, APPLICATION_JSON, null);
	}

	/**
	 * json post请求
	 *
	 * @param url
	 * @param json
	 * @return
	 */
	public static String postJson(String url, String json, Map<String, String> header) {
		return post(url, json, APPLICATION_JSON, header);
	}

	/**
	 * 表单post请求
	 *
	 * @param url
	 * @param map 主体内容
	 * @return
	 */
	public static String postFrom(String url, Map<String, Object> map) {
		return post(url, map, FORM_URLENCODED, null);
	}

	/**
	 * 表单post请求
	 *
	 * @param url
	 * @param map    主体内容
	 * @param header 头信息
	 * @return
	 */
	public static String postFrom(String url, Map<String, Object> map, Map<String, String> header) {
		return post(url, map, FORM_URLENCODED, header);
	}

	/**
	 * HttpClient post请求
	 *
	 * @param url    地址
	 * @param map    参数
	 * @param type   类型
	 * @param header
	 * @return
	 */
	public static String post(String url, Object map, String type, Map<String, String> header) {
		//链式调用
		HttpClient client = getClient();
		//创建 builder
		HttpRequest.Builder reBuilder = HttpRequest.newBuilder();
		if (header != null) {
			Set<String> strings = header.keySet();
			for (String s : strings) {
				reBuilder.header(s, header.get(s));
			}
		}

		String param = "";
		if (type.equals(APPLICATION_JSON)) {
			param = map.toString();
		} else if (type.equals(FORM_URLENCODED)) {
			List<String> paramArr = new ArrayList<>();
			Set<Map.Entry<String, Object>> entrySet = ((Map<String, Object>) map).entrySet();
			for (Map.Entry<String, Object> stringObjectEntry : entrySet) {
				paramArr.add(stringObjectEntry.getKey() + "=" + stringObjectEntry.getValue().toString());
			}
			param = String.join("&", paramArr);
		}

		//链式调用
		HttpRequest request = reBuilder
				//存入消息头
				//消息头是保存在一张 TreeMap 里的
				//.header(CONTENT_TYPE, APPLICATION_JSON)
				.header(CONTENT_TYPE, type)
				//http 协议版本
				.version(HttpClient.Version.HTTP_2)
				//url 地址
				.uri(URI.create(url))
				//超时时间
				.timeout(Duration.ofMillis(5009))
				//发起一个 post 消息，需要存入一个消息体
				.POST(HttpRequest.BodyPublishers.ofString(param))
				//method(...) 方法是 POST(...) 和 GET(...) 方法的底层，效果一样
				//.method("POST",HttpRequest.BodyPublishers.ofString(param.toString()))
				//创建完成
				.build();

		HttpResponse<String> response = null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return response.body();
	}
}
