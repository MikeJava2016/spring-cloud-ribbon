package com.sunshine.security.config.phoneNumber;

import com.alibaba.fastjson.JSONObject;
import com.sunshine.common.util.web.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/20 21:28
 **/
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String mobileParameter = "mobile";

    private String smsCodeParameter = "smsCode";
    /**
     * 是否仅 POST 方式
     */
    private boolean postOnly = true;

    public SmsCodeAuthenticationFilter(String pattern, String httpMethod) {

        super(new AntPathRequestMatcher(pattern, httpMethod));

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

        String smsCode = getSmsCode(jsonObject, smsCodeParameter);

        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken.SmsCodeAuthenticationTokenBuilder()
                .mobile(mobile)
                .smsCode(smsCode)
                .noAuthenticatedbuilder();

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private String getMobile(JSONObject jsonObject, String mobileParameter) {
        String mobile = jsonObject.getString(mobileParameter);

        Assert.hasText(mobileParameter, " mobile parameter must not be empty or null");

        return mobile;
    }

    private String getSmsCode(JSONObject jsonObject, String smsCodeParameter) {
        String mobile = jsonObject.getString(smsCodeParameter);

        Assert.hasText(smsCodeParameter, "smsCode parameter must not be empty or null");

        return mobile;
    }

    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

}