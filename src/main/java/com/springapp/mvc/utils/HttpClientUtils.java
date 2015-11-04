package com.springapp.mvc.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {
//    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static final int CONNECTION_TIMEOUT_MS = 20000;
    private static final int SO_TIMEOUT_MS = 20000;
    private static final String CONTENT_CHARSET = "UTF-8";
    private static final String GB_CONTENT_CHARSET = "GB2312";


    private static CloseableHttpClient buildHttpClient(boolean isMultiThread) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setDefaultRequestConfig(buildRequestConfig());
        if (isMultiThread) {
            httpClientBuilder.setConnectionManager(bulidConnectionManager());
        }
        return httpClientBuilder.build();
    }

    private static PoolingHttpClientConnectionManager bulidConnectionManager() {
        MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000).build();
        ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).setMessageConstraints(messageConstraints).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(600);
        connectionManager.setDefaultMaxPerRoute(200);
        connectionManager.setDefaultConnectionConfig(connectionConfig);
        connectionManager.setValidateAfterInactivity(1000);
        return connectionManager;
    }

    private static RequestConfig buildRequestConfig() {
        RequestConfig requestConfig = RequestConfig.custom().
                setCookieSpec("ignoreCookies").
                setExpectContinueEnabled(true).
                setTargetPreferredAuthSchemes(Arrays.asList(new String[]{"NTLM", "Digest"})).
                setProxyPreferredAuthSchemes(Arrays.asList(new String[]{"Basic"})).
                setSocketTimeout(SO_TIMEOUT_MS).setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
        return requestConfig;
    }

    private static String buildGetUrl(String url, Object obj) {
        Map<String,String> params = (Map<String,String>)obj;
        StringBuffer uriStr = new StringBuffer(url);
        if (params != null) {
            List<NameValuePair> ps = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                ps.add(new BasicNameValuePair(key, params.get(key)));
            }
            uriStr.append("?");
            uriStr.append(URLEncodedUtils.format(ps, CONTENT_CHARSET));
        }
        return uriStr.toString();
    }


    private static void buildHttpPost(HttpPost httpPost, Object obj, boolean isBody, String contentType) throws UnsupportedEncodingException {
        if (obj != null && obj instanceof Map) {
            Map<String,String> params = (Map<String,String>)obj;
            if (isBody) {
                httpPost.setEntity(new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON));
            } else {
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                for (String key : params.keySet()) {
                    formparams.add(new BasicNameValuePair(key, params.get(key)));
                }
                HttpEntity httpEntity = new UrlEncodedFormEntity(formparams, CONTENT_CHARSET);
                httpPost.setEntity(httpEntity);
            }
        }else if(obj instanceof String && StringUtils.isNotBlank((String)obj)){
            if(StringUtils.equalsIgnoreCase(ContentType.APPLICATION_XML.getMimeType(), contentType)){
                httpPost.setEntity(new StringEntity((String)obj, ContentType.APPLICATION_XML));
            }else {
                httpPost.setEntity(new StringEntity((String)obj, ContentType.APPLICATION_JSON));
            }
        }
    }

    private static HttpGet buildHttpGet(String url, Object params) {
        HttpGet get = new HttpGet(buildGetUrl(url, params));
        return get;
    }


    private static String httpInvoke(String url, String httpMethod, Object params, boolean isBody, String charset, String contentType) {
        String result = null;
        try {
            CloseableHttpClient client = buildHttpClient(false);
            HttpRequestBase httpRequest = null;
            if (httpMethod != null && httpMethod.equalsIgnoreCase(HttpPost.METHOD_NAME)) {
                HttpPost httpPost = new HttpPost(url);
                buildHttpPost(httpPost, params, isBody, contentType);
                httpRequest = httpPost;
            } else if (httpMethod != null && httpMethod.equalsIgnoreCase(HttpGet.METHOD_NAME)) {
                httpRequest = buildHttpGet(url, params);
            }
            if (httpRequest == null) {
                return null;
            }
            CloseableHttpResponse response = client.execute(httpRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpRequest.abort();
                throw new RuntimeException("HttpClient Post,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, charset);
            }
            EntityUtils.consume(entity);
            response.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }


    public static String doGet(String url, Map<String, String> params) {
        return httpInvoke(url, HttpGet.METHOD_NAME, params, false, CONTENT_CHARSET, null);
    }

    public static String doGetGB(String url, Map<String, String> params) {
        return httpInvoke(url, HttpGet.METHOD_NAME, params, false, GB_CONTENT_CHARSET, null);
    }

    public static String doPost4Josn2Body(String url, Map<String, String> params) {
        return httpInvoke(url, HttpPost.METHOD_NAME, params, true, CONTENT_CHARSET, null);
    }

    public static String doPost4Form(String url, Map<String, String> params) {
        return httpInvoke(url, HttpPost.METHOD_NAME, params, false, CONTENT_CHARSET, null);
    }

    private static String doPostString(String url, String str, String contentType){
        return httpInvoke(url, HttpPost.METHOD_NAME, str, true, CONTENT_CHARSET, contentType);
    }

    public static String doPostXml(String url, String xml){
        return  doPostString(url, xml, ContentType.APPLICATION_XML.getMimeType());
    }

    public static String doPostJson(String url, String json){
        return doPostString(url, json, ContentType.APPLICATION_JSON.getMimeType());
    }
}