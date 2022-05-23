package com.sunshine.gateway.predicate;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.http.HttpCookie;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/5 8:19
 **/
public class AuthRoutePredicateFactory extends AbstractRoutePredicateFactory<AuthRoutePredicateFactory.Config> {

    /**
     * authorization key.
     */
    public static final String AUTHORIZATION_KEY = "authorization";

    /**
     * Regexp key.
     */
    public static final String REGEXP_KEY = "regexp";

    public AuthRoutePredicateFactory() {
        super(AuthRoutePredicateFactory.Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(AUTHORIZATION_KEY, REGEXP_KEY);
    }


    @Override
    public Predicate<ServerWebExchange> apply(AuthRoutePredicateFactory.Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange exchange) {
                List<HttpCookie> cookies = exchange.getRequest().getCookies()
                        .get(config.authorization);
                if (cookies == null) {
                    return false;
                }
                for (HttpCookie cookie : cookies) {
                    if (cookie.getValue().matches(config.regexp)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String toString() {
                return String.format("Cookie: name=%s regexp=%s", config.authorization,
                        config.regexp);
            }
        };
    }

    @Validated
    public static class Config {

        @NotEmpty
        private String authorization;

        @NotEmpty
        private String regexp;

        public String getAuthorization() {
            return authorization;
        }

        public AuthRoutePredicateFactory.Config setAuthorization(String authorization) {
            this.authorization = authorization;
            return this;
        }

        public String getRegexp() {
            return regexp;
        }

        public AuthRoutePredicateFactory.Config setRegexp(String regexp) {
            this.regexp = regexp;
            return this;
        }

    }
}
