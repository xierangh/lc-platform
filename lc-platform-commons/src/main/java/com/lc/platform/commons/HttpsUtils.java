package com.lc.platform.commons;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

public class HttpsUtils {

	
	public static String post(HttpClient httpClient,String url)throws Exception{
		return post(httpClient,url, null, null);
	}
	
	public static String post(String url, Map<String, Object> params) throws Exception {
		CloseableHttpClient httpClient = createSSLClientDefault();
		return post(httpClient,url, params, null);
	}
	
	
	public static String post(HttpClient httpClient,String url, Map<String, Object> params)throws Exception{
		return post(httpClient,url, params, null);
	}
	
	public static String post(HttpClient httpClient,String url, Map<String, Object> params,
			String jsonStr) throws Exception {
		HttpPost post = new HttpPost(url);
		URIBuilder uriBuilder = new URIBuilder(post.getURI());
		if(params!=null){
			for (String key : params.keySet()) {
				Object value = params.get(key);
				uriBuilder.setParameter(key, value.toString());
			}
		}
		post.setURI(uriBuilder.build());
		if(StringUtils.isNotBlank(jsonStr)){
			HttpEntity content = new StringEntity(jsonStr, "UTF-8");
			post.setEntity(content);
		}
		HttpResponse response = httpClient.execute(post);
		HttpEntity entity = response.getEntity();
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			return EntityUtils.toString(entity);
		}
		return null;
	}

	public static String get(String url, Map<String, Object> params)
			throws Exception {
		CloseableHttpClient httpClient = createSSLClientDefault();
		HttpGet get = new HttpGet(url);
		URIBuilder uriBuilder = new URIBuilder(get.getURI());
		for (String key : params.keySet()) {
			Object value = params.get(key);
			uriBuilder.setParameter(key, value.toString());
		}
		get.setURI(uriBuilder.build());
		CloseableHttpResponse response = httpClient.execute(get);
		HttpEntity entity = response.getEntity();
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			return EntityUtils.toString(entity);
		}
		return null;
	}

	public static CloseableHttpClient createSSLClientDefault() {
		try {
			RedirectStrategy redirectStrategy = new DefaultRedirectStrategy() {
				public boolean isRedirected(HttpRequest request,
						HttpResponse response,
						org.apache.http.protocol.HttpContext context) {
					boolean isRedirect = false;
					try {
						isRedirect = super.isRedirected(request,
								response, context);
					} catch (ProtocolException e) {
						e.printStackTrace();
					}
					if (!isRedirect) {
						int responseCode = response.getStatusLine()
								.getStatusCode();
						if (responseCode == 301 || responseCode == 302) {
							return true;
						}
					}
					return isRedirect;
				}
			};
			RequestConfig requestConfig = RequestConfig.custom()
					.setCookieSpec(CookieSpecs.DEFAULT).build();
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null, new TrustStrategy() {
						public boolean isTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
							return true;
						}
					}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContext);
			return HttpClients.custom().setRedirectStrategy(redirectStrategy)
					.setDefaultRequestConfig(requestConfig).setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
