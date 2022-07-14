package com.sunshine.springvc.servlet3;

import cn.hutool.json.JSONUtil;
import com.sunshine.common.base.Result;
import com.sunshine.utils.web.HttpResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * https://blog.csdn.net/qq_31086797/article/details/108455482
 * @version v1
 * @Description TODO
 * @Author huzhanglin 处理异步长轮询机制
 * @Date 2022/7/13 下午 05:18
 **/
public class AsyncServletUtils {

    private static final Logger logger = LoggerFactory.getLogger(AsyncServletUtils.class);

    private static ScheduledExecutorService scheduledExecutorService;

    private static WebApplicationContext applicationContext;

    private static void  initBean(HttpServletRequest request) {
        if (null == applicationContext) {
            synchronized (AsyncServletUtils.class) {
                logger.info(" AsyncServletUtils init applicationContext and scheduledExecutorService");
                if (null == applicationContext) {
                    applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                    scheduledExecutorService = applicationContext.getBean(ScheduledExecutorService.class);
                }
            }
        }
    }

    /**
     * 让请求hold住  延迟多久返回数据
     * @param request
     * @param timeoutSeconds
     * @param data
     * @param delaySeconds
     */
    public static void doAsyncResponse(HttpServletRequest request, final long timeoutSeconds, final Object data,  long delaySeconds) {
        initBean(request);
        // 释放http连接，转为异步
        AsyncContext context = request.startAsync();
        // 4秒才超时了，超时也会中断当前请求直接返回
        context.setTimeout(timeoutSeconds * 1000L);
        // 异步处理，等待3秒后执行
        scheduledExecutorService.schedule(() -> {
            ServletResponse response = context.getResponse();
            response.setContentType("application/json;charset=utf-8");
            try {
                HttpResponseUtils.write((HttpServletResponse) response,  data,true);
                response.getWriter().print(JSONUtil.toJsonStr(Result.success(data)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // TODO 异步完成，返回客户端信息
            context.complete();
        }, delaySeconds, TimeUnit.SECONDS);
    }
    
}
