package com.sunshine.security.web.filter;

import com.sunshine.common.util.web.MutableHttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @version v1 HttpServletRequest多次读取的办法
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/21 9:26
 **/
@Slf4j
public class ReadRequestBodyFilter  implements Filter, Ordered {

    @Override
    public void init(FilterConfig filterConfiguration) throws ServletException {
        log.info("ReadRequestBodyFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
        log.info("ReadRequestBodyFilter doFilter begin");
        request = new MutableHttpServletRequest((HttpServletRequest) request);
        chain.doFilter(request, response);
        log.info("ReadRequestBodyFilter doFilter end");

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