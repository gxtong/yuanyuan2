package com.jhx.common.util.http;

import com.jhx.common.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class HttpsUtil {

	/**
	 * 发送Post请求，并返回字符串
	 * 
	 * @param url
	 *            示例：http://a.c.com/user/register
	 * @param urlParamOrJsonData
	 *            url参数或者json字符串 示例：name=jack&password=123 或
	 *            {"name":"jack","password":"123"}
	 * @param charset
	 *            示例：UTF-8
	 * @return
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, String urlParamOrJsonData, String charset){
		if (JsonUtil.isJson(urlParamOrJsonData)) {
			return postJson(url, urlParamOrJsonData, charset);
		} else {
			return postUrl(url, urlParamOrJsonData, charset);
		}
	}

	private static String postUrl(String url, String urlParams, String charset) {
		String strResult = "";
		HttpResponse resp = null;
		HttpClient httpClient = createAuthNonHttpClient(charset);
		try{
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (String pair : StringUtils.split(urlParams, "&")) {
				String[] arr = StringUtils.split(pair, "=");
				if (arr.length > 0) {
					params.add(new BasicNameValuePair(arr[0], arr.length > 1 ? arr[1] : ""));
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(params, charset));
		    resp = httpClient.execute(httpPost);
		    HttpEntity entity = resp.getEntity();
			strResult = EntityUtils.toString(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			HttpClientUtils.closeQuietly(resp);
			HttpClientUtils.closeQuietly(httpClient);
		}
		return strResult;
	}

	public static String postJson(String url, String json, String charset) {
		String strResult = "";
		HttpResponse resp = null;
		HttpClient httpClient = createAuthNonHttpClient(charset);
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
			HttpEntity postEntity = new StringEntity(json, "utf-8");
			httpPost.setEntity(postEntity);
			resp = httpClient.execute(httpPost);
			HttpEntity entity = resp.getEntity();
			strResult = EntityUtils.toString(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HttpClientUtils.closeQuietly(resp);
			HttpClientUtils.closeQuietly(httpClient);
		}
		return strResult;
	}

	public static String postXml(String url, String xmlContent, String charset) {
		String strResult = "";
		HttpResponse resp = null;
		charset = charset.toUpperCase();
		HttpClient httpClient = createAuthNonHttpClient(charset);
		try{
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new StringEntity(xmlContent,charset));
			httpPost.setHeader("Content-type", "application/tlt-notify");
			resp = httpClient.execute(httpPost);
			HttpEntity entity = resp.getEntity();
			strResult = EntityUtils.toString(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			HttpClientUtils.closeQuietly(resp);
			HttpClientUtils.closeQuietly(httpClient);
		}
		return strResult;
	}

	@SuppressWarnings("deprecation")
	public static HttpClient createAuthNonHttpClient(String charset) {
		SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(60000).build();
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
		ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
		registryBuilder.register("http", plainSF);
		// 指定信任密钥存储对象和连接套接字工厂
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, new AnyTrustStrategy())
					.build();
			LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registryBuilder.register("https", sslSF);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		Registry<ConnectionSocketFactory> registry = registryBuilder.build();
		// 设置连接参数
		ConnectionConfig connConfig = ConnectionConfig.custom().setCharset(Charset.forName(charset)).build();
		// 设置连接管理器
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
		connManager.setDefaultConnectionConfig(connConfig);
		connManager.setDefaultSocketConfig(socketConfig);
		// 指定cookie存储对象
		BasicCookieStore cookieStore = new BasicCookieStore();
		return HttpClientBuilder.create().setDefaultCookieStore(cookieStore).setConnectionManager(connManager).build();
	}

	private static class AnyTrustStrategy implements TrustStrategy {

		@Override
		public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			return true;
		}

	}

	public static void main(String args[]) {
		System.out.println(post("https://www.baifubao.com/callback", "cmd=1059", "UTF-8"));
	}
}