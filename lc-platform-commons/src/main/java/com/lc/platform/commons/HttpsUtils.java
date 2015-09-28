package com.lc.platform.commons;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpsUtils {

	
	public static String post(String url,Map<String, Object> params, String jsonStr) throws Exception {
		CloseableHttpClient httpClient = createSSLClientDefault();
		HttpPost post = new HttpPost(url);
		URIBuilder uriBuilder = new URIBuilder(post.getURI());
		for (String key : params.keySet()) {
			Object value = params.get(key);
			uriBuilder.setParameter(key,value.toString());
		}
		post.setURI(uriBuilder.build());
		
		HttpEntity content = new StringEntity(jsonStr,"UTF-8");
		post.setEntity(content);
		CloseableHttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();  
        int statusCode = response.getStatusLine().getStatusCode();  
        if(statusCode == HttpStatus.SC_OK){
        	return EntityUtils.toString(entity);
        }
        return null;
	}
	
	public static String get(String url, Map<String, Object> params) throws Exception {
		CloseableHttpClient httpClient = createSSLClientDefault();
		HttpGet get = new HttpGet(url);
		URIBuilder uriBuilder = new URIBuilder(get.getURI());
		for (String key : params.keySet()) {
			Object value = params.get(key);
			uriBuilder.setParameter(key,value.toString());
		}
		get.setURI(uriBuilder.build());
		CloseableHttpResponse response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();  
        int statusCode = response.getStatusLine().getStatusCode();  
        if(statusCode == HttpStatus.SC_OK){
        	return EntityUtils.toString(entity);
        }
        return null;
	}

	public static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null, new TrustStrategy() {
						public boolean isTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
							return true;
						}
					}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();
	}
}
