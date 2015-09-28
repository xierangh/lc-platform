package com.lc.platform.commons;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * http工具类，支持post和get请求
 * @author 陈均
 *
 */
public class HttpUtils {
	public static Charset UTF_8 = Charset.forName("UTF-8");
	
	/**
	 * post请求
	 * @param url 请求地址
	 * @param params 传递的参数集合，内容可以是String、File
	 * @param charset 传递的参数编码
	 * @return 返回字符串内容
	 * @throws Exception
	 */
	public static String post(String url, Map<String, Object> params,Charset charset) throws Exception{
		CloseableHttpClient client = HttpClients.createDefault();
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		HttpPost post = new HttpPost(url);
		if (params != null) {
			for (String name : params.keySet()) {
				Object value = params.get(name);
				String mimeType = ContentType.MULTIPART_FORM_DATA.getMimeType();
				if (value instanceof String) {
					builder.addPart(name, new StringBody(value.toString(),
							ContentType.create(mimeType, UTF_8)));
				} else if (value instanceof File) {
					FileBody fileBody = new FileBody((File) value);
					builder.addPart(name, fileBody);
				}else{
					builder.addPart(name, new StringBody(value.toString(),
							ContentType.create(mimeType, UTF_8)));
				}
			}
		}
		post.setEntity(builder.build());
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (HttpStatus.SC_OK == statusCode) {
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		}
		throw new Exception(statusCode+"");
	}
	/**
	 * post请求，使用UTF-8编码传递参数
	 * @param url 请求地址
	 * @param params 传递的参数集合，内容可以是String、File
	 * @return 返回字符串内容
	 * @throws Exception
	 */
	public static String post(String url, Map<String, Object> params)
			throws Exception {
		return post(url, params, UTF_8);
	}

}
