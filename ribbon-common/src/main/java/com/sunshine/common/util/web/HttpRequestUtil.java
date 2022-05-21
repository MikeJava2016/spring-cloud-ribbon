package com.sunshine.common.util.web;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/20 22:47
 **/
public class HttpRequestUtil {

    /**
     * 获取请求体中的数据
     *
     * @param request
     * @return
     */
    public static String getRequestBody(HttpServletRequest request) {
        try {
            ServletRequest requestWrapper = new MutableHttpServletRequest (
                    request);
            StringBuffer jb = new StringBuffer();
            String line = null;
            try {
                BufferedReader reader = requestWrapper.getReader();
                while ((line = reader.readLine()) != null)
                    jb.append(line);
            } catch (Exception e) { /*report an error*/ }
            return jb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
