package com.sunshine.gateway;

import com.sunshine.api.feign.client.AuthLoginClientAPI;
import com.sunshine.filter.AuthLoginGlobalFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.cloud.gateway.config.GatewayClassPathWarningAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.DispatcherHandler;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/5 8:40
 **/
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "spring.cloud.gateway.enabled", matchIfMissing = true)
@EnableConfigurationProperties
@AutoConfigureBefore({HttpHandlerAutoConfiguration.class,
        WebFluxAutoConfiguration.class})
@AutoConfigureAfter({GatewayLoadBalancerClientAutoConfiguration.class,
        GatewayClassPathWarningAutoConfiguration.class})
@ConditionalOnClass(DispatcherHandler.class)
@PropertySource(value = "classpath:loginfilter.properties")
public class SunshineGatewayConfiguration2 {

    private final static Logger logger = LoggerFactory.getLogger(SunshineGatewayConfiguration2.class);


//    @Bean
    public GlobalFilter customFilter2(@Value("#{'${jwt.ignoreUrls}'.split(',')}")
                                              List<String> ignoreUrls, @Autowired AuthLoginClientAPI authLoginClientAPI) {
        AuthLoginGlobalFilter authLoginGlobalFilter = new AuthLoginGlobalFilter(ignoreUrls,authLoginClientAPI);
        logger.info(" create bean authLoginGlobalFilter");
        return authLoginGlobalFilter;
    }


    @Value("${server.https.port}")
    private int httpsPort;
    @Value("${server.port}")
    private int serverPort;

    @PostConstruct
    public void startRedirectServer() {
        NettyReactiveWebServerFactory httpNettyReactiveWebServerFactory = new NettyReactiveWebServerFactory(httpsPort);
        httpNettyReactiveWebServerFactory.getWebServer((request, response) -> {
            URI uri = request.getURI();
            URI httpsUri;
            try {
                httpsUri = new URI("https", uri.getUserInfo(), uri.getHost(), httpsPort, uri.getPath(), uri.getQuery(), uri.getFragment());
            } catch (URISyntaxException e) {
                return Mono.error(e);
            }
            response.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            response.getHeaders().setLocation(httpsUri);
            return response.setComplete();
        }).start();
    }

}
