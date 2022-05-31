package com.sunshine.security.web.filter;

import com.sunshine.common.util.web.MutableHttpServletRequest;
import com.sunshine.utils.common.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @version v1 HttpServletRequest多次读取的办法
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/21 9:26
 * http://www.51gjie.com/javaweb/961.html
 * https://www.csdn.net/tags/OtDaEg4sNzE0NzEtYmxvZwO0O0OO0O0O.html
 * http://www.manongjc.com/detail/27-bxxuzkahozoisiz.html https://blog.csdn.net/xiaoQL520/article/details/107532838
 **/
@Slf4j
public class ReadRequestBodyFilter  implements Filter, Ordered {

    private String exclude = "file";

    @Override
    public void init(FilterConfig filterConfiguration) throws ServletException {
        log.info("ReadRequestBodyFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
        log.info("ReadRequestBodyFilter doFilter begin");
        MutableHttpServletRequest mutableHttpServletRequest = new MutableHttpServletRequest((HttpServletRequest) request);
        this.debug(mutableHttpServletRequest);
        chain.doFilter(mutableHttpServletRequest, response);
        log.info("ReadRequestBodyFilter doFilter end");

    }

    private void debug(MutableHttpServletRequest request) {
        if (request.getRequestURI().contains(exclude)){
            // 排除
        }
        this.doDebug(request);
    }

    private void doDebug(MutableHttpServletRequest request) {
        String method = request.getMethod();
        switch (method){
            case "GET" : doGetDebug(request);
                break;
            case "POST": doPostDebug(request);
             break;
            default: ;
        }
    }

    private void doGetDebug(MutableHttpServletRequest request) {
        Map map = request.getParameterMap();
        log.info(" uri = {},method = {}, param = {} ",request.getRequestURI(),request.getMethod(), JsonUtil.toJson(map));
    }

    private void doPostDebug(MutableHttpServletRequest request){

            Map<String, String[]> map = request.getParameterMap();
            Map<String, Object> params = new HashMap<String, Object>();
            int length;
            //将Map<String, String[]>转为普通map
            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                length = entry.getValue().length;
                if (length == 1) {
                    params.put(entry.getKey(), entry.getValue()[0]);
                } else if (length > 1) {
                    params.put(entry.getKey(), entry.getValue());
                }
            }
        log.info(" uri = {},method = {}, param = {}",request.getRequestURI(),request.getMethod(), JsonUtil.toJson(params));
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public int getOrder() {
        return 0;
    }
}