package com.sunshine.utils.web;

import com.sunshine.utils.common.JsonUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/13 下午 06:56
 **/
public class HttpResponseUtils {

    public static void write(HttpServletResponse response,String msg, boolean disableCache) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        if (disableCache) {
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-cache,no-store");
        }
        response.setStatus(HttpServletResponse.SC_OK);
        out.write(msg);
        out.flush();
        out.close();
    }

    public static void write(HttpServletResponse response,Object data, boolean disableCache) throws IOException {
        write(response, JsonUtil.toJson(data),disableCache);
    }
}
