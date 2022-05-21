package com.sunshine.security.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunshine.common.util.JwtUtils;
import com.sunshine.common.util.web.ApplicationContextUtils;
import com.sunshine.common.util.web.PropertyUtils;
import com.sunshine.security.config.common.CommonSpringSecurity;
import com.sunshine.security.config.phoneNumber.SmsCodeAuthenticationToken;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @version v1
 * @Description jwtToken校验过滤器
 * @Author huzhanglin
 * @Date 2022/5/18 8:42
 **/
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
  /*  @Autowired
    private RedisCache redisCache;*/
    /**
     * 不需要权限
     */
    private static final String BASE_EXCLUDE_PATH = "/js/**,/css/**,/images/**";
    /**
     * 配置文件
     */
    private static final String SPRING_SECURITY_CUSTOMER_PROPERTIES = "spring-security.properties";

    /**
     * 自定义不需要权限既可以访问
     */
    private static final String CUSTOMER_EXCLUDE_PATH = "customer_exclude_path";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String token_invalidate = PropertyUtils.getPropertiesValue("spring-security.properties", "token_invalidate", "2");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        // 判断是否需要验证jwtToken
        if (isMatch(requestURI)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //获取token
        String token = request.getHeader("token");

        if (!StringUtils.hasText(token)) {
            logger.error("token没有");
            // 请求wutoken
            CommonSpringSecurity.failResponse(response, "参数no token", null, null);
            return;
        }
        String message = validateJwtToken(token);
        if (StringUtils.hasText(message)) {
            logger.error("token失效 token = {}", token);
            CommonSpringSecurity.failResponse(response, "token失效", null, null);
            return;
        }
        // jwt token有效的话
        RedissonClient redissonClient = ApplicationContextUtils.getBean(RedissonClient.class);
        RBucket<String> bucket = redissonClient.getBucket(token);
        // 判断是否存在
        boolean exists = bucket.isExists();
        if (!exists) {
            logger.error("redis token失效 token = {}", token);
            CommonSpringSecurity.failResponse(response, "token失效", null, null);
            return;
        }
        //刷新token
        bucket.expire(Integer.parseInt(token_invalidate), TimeUnit.MINUTES);
        String json = bucket.get();

        //存入SecurityContex上下文
        SecurityContextHolder.getContext().setAuthentication(this.prase(json));
        //放行
        filterChain.doFilter(request, response);
    }


    /**
     * 请求的url满足
     *
     * @param requestURI
     * @return
     */
    private static boolean isMatch(String requestURI) {
        final AntPathMatcher pathMatcher = new AntPathMatcher();
        List<String> urls = getUrls();
        for (String url : urls) {
            if (pathMatcher.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取所有的url
     *
     * @return
     */
    private static List<String> getUrls() {

        String customerExcludePath = PropertyUtils.getPropertiesValue(SPRING_SECURITY_CUSTOMER_PROPERTIES, CUSTOMER_EXCLUDE_PATH, "");
        List<String> allList = Arrays.stream(BASE_EXCLUDE_PATH.split(",")).collect(Collectors.toList());
        allList.addAll(Arrays.stream(customerExcludePath.split(",")).collect(Collectors.toList()));
        return allList;
    }

    private Authentication prase(String json) {
        //{"authorities":[{"authority":"admin"}],"details":{"password":"123","username":"huzhanglin","authorities":[{"authority":"admin"}],"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true,"sysUser":{"id":"23","username":"huzhanglin","password":"123"}},"authenticated":true,"username":"huzhanglin","uid":"23","name":""}
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray authorities = jsonObject.getJSONArray("authorities");
        String username = jsonObject.getString("username");
        String uid = jsonObject.getString("uid");
        String mobile = jsonObject.getString("mobile");
        final List<GrantedAuthority> authorityList = new ArrayList<>(authorities.size());
        if (authorities.size() > 0) {
            for (int i = 0; i < authorities.size(); i++) {
                JSONObject innerJsonObject = authorities.getJSONObject(i);
                String authority = innerJsonObject.getString("authority");
                authorityList.add(new SimpleGrantedAuthority(authority));
            }
        }
        SmsCodeAuthenticationToken.SmsCodeAuthenticationTokenBuilder builder = new SmsCodeAuthenticationToken.SmsCodeAuthenticationTokenBuilder();
        AuthenticationToken authenticationToken = builder.username(username)
                .mobile(mobile)
                .uid(uid)
                .authenticatedbuilder(authorityList);
        return authenticationToken;
    }

    private String validateJwtToken(String token) {
        //解析token
        Map<String, Object> map = JwtUtils.parseToken(token, "security");
        if (CollectionUtils.isEmpty(map) || map.get("uid") == null || "".equals(map.get("uid"))) {
            logger.info(" token  无效，token = {}", token);
            return "token 无效";
        }

        String uid = map.get("uid").toString();
        logger.info(" uid = {}", uid);
        //根据userId在redis中获取用户信息
        // LoginUser loginUser = redisCache.getCacheObject("login:" + userId);
        return null;
    }
}
