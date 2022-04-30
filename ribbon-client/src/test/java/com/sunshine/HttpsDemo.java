package com.sunshine;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpsDemo {

    private final static Logger logger = LoggerFactory.getLogger(HttpsDemo.class);

    public static void main(String[] args) {
        OkHttpClient okHttpClient = getHttps();
        String path = "http://localhost:8085/user";

        // 2 创建okhttpclient对象
        OkHttpClient client = getHttps();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject json = new JSONObject();

        json.put("username", "root");
        json.put("password", "root");
        //json为String类型的json数据
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();

        // 4 执行请求操作
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String string = response.body().string();
                System.out.println(string);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static OkHttpClient getHttps() {


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .hostnameVerifier((hostname, session) -> true)
                .sslSocketFactory(createSSLSocketFactory())
                .build();

        return okHttpClient;
    }

    //进行post请求
    public static String doPostHttps(String url, Headers headers, RequestBody body) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (headers != null) {
            builder.headers(headers);
        }
        builder.post(body);
        int maxRetryTime = 1;
        int time = 0;
        String result = null;
        do {
            time++;
            try {
                Response response = getHttps().newCall(builder.build()).execute();
                if (response.isSuccessful() && response.body() != null) {
                    result = response.body().string();
                } else {
                    logger.error("接口请求{}失败 ^_^ headers:{} ^_^ 返回值:{}", url, "", result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (null == result && time < maxRetryTime);
        return null == result ? "" : result;
    }


    //这里是创建一个SSLSocketFactory,提供给上面的 .sslSocketFactory()
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new X509TrustManager(){
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }
}
