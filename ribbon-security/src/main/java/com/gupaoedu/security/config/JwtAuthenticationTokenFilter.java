package com.gupaoedu.security.config;

import com.alibaba.fastjson.JSON;
import com.gupaoedu.security.entity.LoginUser;
import com.sunshine.common.util.JwtUtils;
import com.sunshine.common.util.Result;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/18 8:42
 **/
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
  /*  @Autowired
    private RedisCache redisCache;*/

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().contains("/login")){
            //放行
            filterChain.doFilter(request,response);
            return;
        }
        //获取token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            logger.info("token没有");
            //放行
           // filterChain.doFilter(request,response);
            // 请求wutoken
            response.getWriter().write(JSON.toJSONString(Result.fail(-1,"参数no token")));
            return;
        }

        //解析token
        Claims claims = JwtUtils.parseToken(token);
        String username = claims.get("username").toString();
        logger.info(" username = {}",username);
        //根据userId在redis中获取用户信息
       // LoginUser loginUser = redisCache.getCacheObject("login:" + userId);
        if(null == username){
            throw new RuntimeException("用户未登录");
        }

        //存入SecurityContex上下文
       /* //TODO 获取权限信息
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(username,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(userToken);

        //放行
        filterChain.doFilter(request,response);*/
    }
}
