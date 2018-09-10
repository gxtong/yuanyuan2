package com.jhx.common.util.http;

import com.jhx.common.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class HttpUtil {

    /**
     * 发送Post请求，并返回字符串
     *
     * @param url                示例：http://a.c.com/user/register
     * @param urlParamOrJsonData url参数或者json字符串
     *                           示例：name=jack&password=123 或 {"name":"jack","password":"123"}
     * @param charset            示例：UTF-8
     * @return
     * @throws ParseException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String post(String url, String urlParamOrJsonData, String charset) throws ParseException, IOException {
        if (StringUtils.isNotBlank(urlParamOrJsonData) && JsonUtil.isJson(urlParamOrJsonData)) {
            return postJson(url, urlParamOrJsonData, charset);
        } else {
            return postUrl(url, urlParamOrJsonData, charset);
        }
    }


    private static String postUrl(String url, String urlParams, String charset)
            throws IOException {

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);

        if (StringUtils.isNotBlank(urlParams)) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            for (String pair : StringUtils.split(urlParams, "&")) {
                String[] arr = StringUtils.split(pair, "=", 2);
                if (arr.length > 0) {
                    params.add(new BasicNameValuePair(arr[0], arr.length > 1 ? arr[1] : ""));
                }
            }
            request.setEntity(new UrlEncodedFormEntity(params, charset));
        }
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            return EntityUtils.toString(entity);
        } else {
            return null;
        }
    }

    /**
     * 发送Get请求，并返回字符串
     *
     * @author liu xp
     * @date 2018/1/5 0005 10:31
     */
    public static String get(String url, String urlParams) throws IOException {
        String result = null;
        String getUrl = (StringUtils.isBlank(urlParams) ? url : (url + "?" + urlParams));
        HttpGet request = new HttpGet(getUrl);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        return result;
    }

    /**
     * 发送Post请求，并返回字符串，采用默认字符编码UTF-8
     *
     * @param url                示例：http://a.c.com/user/register
     * @param urlParamOrJsonData url参数或者json字符串
     *                           示例：name=jack&password=123 或 {"name":"jack","password":"123"}
     * @return
     * @throws ParseException
     * @throws ClientProtocolException
     * @throws IOException
     */

    public static String post(String url, String urlParamOrJsonData) throws IOException {
        return post(url, urlParamOrJsonData, "UTF-8");
    }

    public static String post(String url) throws IOException {
        return post(url, null, "UTF-8");
    }

    private static String postJson(String url, String jsonData, String charset) throws ParseException, IOException {
        HttpClient httpClient = HttpClients.createDefault();

        HttpPost request = new HttpPost(url);
        StringEntity params = new StringEntity(jsonData, charset);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            return EntityUtils.toString(entity);
        } else {
            return null;
        }
    }

    /**
     * 将Map转为Url参数（（如a=1&b=2））
     *
     * @param map
     * @return
     */
    public static String mapToUrlParams(Map<String, String> map) {
        StringJoiner sj = new StringJoiner("&");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sj.add(entry.getKey() + "=" + entry.getValue());
        }
        return sj.toString();
    }

    /**
     * 将url参数（如a=1&b=2）转成一个有序的map
     *
     * @param urlParams
     * @return
     */
    public static TreeMap<String, String> urlParamsToMap(String urlParams) {
        TreeMap<String, String> ret = new TreeMap<>();
        for (String pair : StringUtils.split(urlParams, "&")) {
            String[] arr = StringUtils.split(pair, "=");
            if (arr.length > 0) {
                ret.put(arr[0], arr.length > 1 ? arr[1] : "");
            }
        }
        return ret;
    }
}
