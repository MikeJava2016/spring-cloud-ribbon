package com.sunshine.security.config.phoneNumber;

import com.alibaba.fastjson.JSONObject;
import com.sunshine.common.util.web.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version v1
 * @Description 手机号的校验
 * @Author huzhanglin
 * @Date 2022/5/20 21:28
 **/
public class PhoneNumberAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String mobileParameter = "mobile";

    /**
     * 是否仅 POST 方式
     */
    private boolean postOnly = true;

    public PhoneNumberAuthenticationFilter(String pattern, String httpMethod, AuthenticationManager authenticationManager) {

        super(new AntPathRequestMatcher(pattern, httpMethod));
        this.setAuthenticationManager(authenticationManager);
        logger.debug("短信验证吗的请求方式必须是{}", httpMethod);

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        // 获取请求体中的数据
        String requestBodyJson = HttpRequestUtil.getRequestBody(request);

        JSONObject jsonObject = JSONObject.parseObject(requestBodyJson);

        String mobile = getMobile(jsonObject, mobileParameter);


        PhoneNumberAuthenticationToken authRequest = new PhoneNumberAuthenticationToken.SmsCodeAuthenticationTokenBuilder()
                .mobile(mobile)
                .noAuthenticatedbuilder();

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private String getMobile(JSONObject jsonObject, String mobileParameter) {
        String mobile = jsonObject.getString(mobileParameter);

        Assert.hasText(mobileParameter, " mobile parameter must not be empty or null");

        return mobile;
    }

    protected void setDetails(HttpServletRequest request, PhoneNumberAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

}
