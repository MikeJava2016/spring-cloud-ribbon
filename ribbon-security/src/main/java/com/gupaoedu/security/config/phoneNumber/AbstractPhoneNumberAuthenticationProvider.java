package com.gupaoedu.security.config.phoneNumber;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/19 9:20
 **/
public abstract class AbstractPhoneNumberAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    protected final Log logger = LogFactory.getLog(getClass());

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private UserCache phoneNumberCache = new PhoneNumberCache();
    private boolean forcePrincipalAsString = false;
    protected boolean hideUserNotFoundExceptions = true;
    private UserDetailsChecker preAuthenticationChecks = new AbstractPhoneNumberAuthenticationProvider.DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new AbstractPhoneNumberAuthenticationProvider.DefaultPostAuthenticationChecks();
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();


    protected abstract void additionalAuthenticationChecks(UserDetails userDetails,
                                                           PhoneNumberAuthenticationToken authentication)
            throws AuthenticationException;

    public final void afterPropertiesSet() throws Exception {
        Assert.notNull(this.phoneNumberCache, "A user cache must be set");
        Assert.notNull(this.messages, "A message source must be set");
        doAfterPropertiesSet();
    }

    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        Assert.isInstanceOf(PhoneNumberAuthenticationToken.class, authentication,
                () -> messages.getMessage(
                        "AbstractPhoneNumberAuthenticationProvider.onlySupports",
                        "Only PhoneNumberAuthenticationToken is supported"));

        PhoneNumberAuthenticationToken phoneNumberAuthenticationToken = (PhoneNumberAuthenticationToken) authentication;
        String phoneNumber = (phoneNumberAuthenticationToken.getPrincipal() == null) ? "NONE_PROVIDED"
                : (String) authentication.getPrincipal();

        boolean cacheWasUsed = true;
        UserDetails user = this.phoneNumberCache.getUserFromCache(phoneNumber);

        if (user == null) {
            cacheWasUsed = false;

            try {
                user = retrieveUser(phoneNumber,
                        (PhoneNumberAuthenticationToken) authentication);
            }
            catch (UsernameNotFoundException notFound) {
                logger.debug("Phone '" + phoneNumber + "' not found");

                if (hideUserNotFoundExceptions) {
                    throw new BadCredentialsException(messages.getMessage(
                            "phoneNumber",
                            "Bad credentials"));
                }
                else {
                    throw notFound;
                }
            }

            Assert.notNull(user,
                    "retrieveUser returned null - a violation of the interface contract");
        }

        try {
            preAuthenticationChecks.check(user);
            additionalAuthenticationChecks(user,
                    (PhoneNumberAuthenticationToken) authentication);
        }
        catch (AuthenticationException exception) {
            if (cacheWasUsed) {
                cacheWasUsed = false;
                user = retrieveUser(phoneNumber,
                        (PhoneNumberAuthenticationToken) authentication);
                preAuthenticationChecks.check(user);
                additionalAuthenticationChecks(user,
                        (PhoneNumberAuthenticationToken) authentication);
            }
            else {
                throw exception;
            }
        }

        postAuthenticationChecks.check(user);

        if (!cacheWasUsed) {
            this.phoneNumberCache.putUserInCache(user);
        }

        Object principalToReturn = user;

        if (forcePrincipalAsString) {
            principalToReturn = user.getUsername();
        }

        return createSuccessAuthentication(principalToReturn, authentication, user);
    }


    protected Authentication createSuccessAuthentication(Object principal,
                                                         Authentication authentication, UserDetails user) {
        PhoneNumberAuthenticationToken phoneNumberAuthentication = (PhoneNumberAuthenticationToken)authentication;

        PhoneNumberAuthenticationToken result = new PhoneNumberAuthenticationToken(
                principal, authentication.getCredentials(),phoneNumberAuthentication.getUuid(),
                authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());

        return result;
    }

    protected void doAfterPropertiesSet() throws Exception {
    }

    public UserCache getUserCache() {
        return phoneNumberCache ;
    }

    public boolean isForcePrincipalAsString() {
        return forcePrincipalAsString;
    }

    public boolean isHideUserNotFoundExceptions() {
        return hideUserNotFoundExceptions;
    }


    protected abstract UserDetails retrieveUser(String username,
                                                PhoneNumberAuthenticationToken authentication)
            throws AuthenticationException;

    public void setForcePrincipalAsString(boolean forcePrincipalAsString) {
        this.forcePrincipalAsString = forcePrincipalAsString;
    }


    public void setHideUserNotFoundExceptions(boolean hideUserNotFoundExceptions) {
        this.hideUserNotFoundExceptions = hideUserNotFoundExceptions;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setUserCache(UserCache userCache) {
        this.phoneNumberCache = userCache;
    }

    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication));
    }

    protected UserDetailsChecker getPreAuthenticationChecks() {
        return preAuthenticationChecks;
    }


    public void setPreAuthenticationChecks(UserDetailsChecker preAuthenticationChecks) {
        this.preAuthenticationChecks = preAuthenticationChecks;
    }

    protected UserDetailsChecker getPostAuthenticationChecks() {
        return postAuthenticationChecks;
    }

    public void setPostAuthenticationChecks(UserDetailsChecker postAuthenticationChecks) {
        this.postAuthenticationChecks = postAuthenticationChecks;
    }

    public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
        this.authoritiesMapper = authoritiesMapper;
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                logger.debug("User account is locked");

                throw new LockedException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.locked",
                        "User account is locked"));
            }

            if (!user.isEnabled()) {
                logger.debug("User account is disabled");

                throw new DisabledException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.disabled",
                        "User is disabled"));
            }

            if (!user.isAccountNonExpired()) {
                logger.debug("User account is expired");

                throw new AccountExpiredException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.expired",
                        "User account has expired"));
            }
        }
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                logger.debug("User account credentials have expired");

                throw new CredentialsExpiredException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                        "User credentials have expired"));
            }
        }
    }
}
