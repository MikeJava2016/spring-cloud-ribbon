package com.gupaoedu.security.config.phoneNumber;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/19 8:51
 **/
public class PhoneNumberAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    // ~ Static fields/initializers
    // =====================================================================================

    public static final String SPRING_SECURITY_FORM_PHONE_NUMBER_KEY = "phoneNumber";
    public static final String SPRING_SECURITY_FORM_CODE_KEY = "code";
    public static final String SPRING_SECURITY_FORM_UUID_KEY = "uuid";

    private String phoneNumberParameter = SPRING_SECURITY_FORM_PHONE_NUMBER_KEY;
    private String codeParameter = SPRING_SECURITY_FORM_CODE_KEY;
    private String uuidParameter = SPRING_SECURITY_FORM_UUID_KEY;
    private boolean postOnly = true;


    public PhoneNumberAuthenticationFilter(String pattern) {
        super(new AntPathRequestMatcher(pattern, "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String phoneNumber = obtainPhoneNumber(request);
        String code = obtainCode(request);
        String uuid = obtainUuid(request);

        if (phoneNumber == null) {
            phoneNumber = "";
        }

        if (code == null) {
            code = "";
        }

        if (uuid == null) {
            uuid = "";
        }

        phoneNumber = phoneNumber.trim();

        PhoneNumberAuthenticationToken authRequest = new PhoneNumberAuthenticationToken(
                phoneNumber, code,uuid);

         setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }


    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(codeParameter);
    }

    protected String obtainUuid(HttpServletRequest request) {
        return request.getParameter(uuidParameter);
    }



    protected String obtainPhoneNumber(HttpServletRequest request) {
        return request.getParameter(phoneNumberParameter);
    }


    protected void setDetails(HttpServletRequest request,
                              PhoneNumberAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }


    public void setPhoneNumberParameter(String phoneNumberParameter) {
        Assert.hasText(phoneNumberParameter, "PhoneNumber parameter must not be empty or null");
        this.phoneNumberParameter = phoneNumberParameter;
    }

    public void setCodeParameter(String codeParameter) {
        Assert.hasText(codeParameter, "code parameter must not be empty or null");
        this.codeParameter = codeParameter;
    }


    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getPhoneNumberParameter() {
        return phoneNumberParameter;
    }

    public final String getCodeParameter() {
        return codeParameter;
    }

    public final String getUuidParameter() {
        return uuidParameter;
    }
}
