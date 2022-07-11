/*
package com.sunshine.common.util.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OkHttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpClientUtil.class);

    */
/**
     * @param url             请求的url eg http://172.17.0.10:23123/ctrl/kpi/kpiSumOrDay/count
     * @param paramJsonObject
     * @return
     *//*

    public static JSONObject post(String url, JSONObject paramJsonObject) {
        logger.info("url = {}",url);
        logger.info("paramJsonObject = {}",JSONObject.toJSONString(paramJsonObject));
        JSONObject resultForJson = null;
        //设置媒体类型。application/json表示传递的是一个json格式的对象
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        //创建okHttpClient对象
        OkHttpClient okhttp = new OkHttpClient();
        //设置okhttp超时
        okhttp.newBuilder().connectTimeout(10000L, TimeUnit.MILLISECONDS).readTimeout(50000, TimeUnit.MILLISECONDS).build();
        //创建RequestBody对象，将参数按照指定的MediaType封装
        RequestBody requestBody = RequestBody.create(mediaType, paramJsonObject.toString());
        //创建一个Request
        Request request = new Request.Builder().post(requestBody).url(url).build();
        try {
            Response response = okhttp.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("unexpected code.." + response);
            String result = response.body().string();
            logger.info(" request out system. the url = {}. result  = {}",url,JSONObject.toJSONString(result));
            resultForJson = JSONObject.parseObject(result);
            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultForJson;
    }
}
*/
