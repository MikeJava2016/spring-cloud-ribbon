package com.sunshine.gateway;

import com.sunshine.api.feign.service.AuthLoginClientAPI;
import com.sunshine.api.feign.service.IUserAuthFeignService;
import com.sunshine.api.feign.service.UserFeignSerivce;
import com.sunshine.gateway.filter.AuthLoginGlobalFilter;
import com.sunshine.gateway.filter.RSAGateWayFilterFactory;
import com.sunshine.gateway.filter.SunshineGlobalFilter;
import com.sunshine.gateway.predicate.AuthRoutePredicateFactory;
import com.sunshine.gateway.route.HttpsClientRequestFactory;
import com.sunshine.gateway.route.SunshineRouteDefinitionRepository;
import com.sunshine.service.RouteService;
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
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
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
//@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "spring.cloud.gateway.enabled", matchIfMissing = true)
@EnableConfigurationProperties
@AutoConfigureBefore({HttpHandlerAutoConfiguration.class,
        WebFluxAutoConfiguration.class})
@AutoConfigureAfter({GatewayLoadBalancerClientAutoConfiguration.class,
        GatewayClassPathWarningAutoConfiguration.class})
@ConditionalOnClass(DispatcherHandler.class)
@PropertySource(value = "classpath:loginfilter.properties")
public class SunshineGatewayConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(SunshineGatewayConfiguration.class);


    @Autowired
    @Lazy
    private UserFeignSerivce userFeignSerivce;

    @Autowired
    @Lazy
    private IUserAuthFeignService iUserAuthFeignService;

//    @Bean
    public AuthRoutePredicateFactory authRoutePredicateFactory() {
        AuthRoutePredicateFactory authRoutePredicateFactory = new AuthRoutePredicateFactory();
        logger.info(" create bean authRoutePredicateFactory");
        return authRoutePredicateFactory;
    }

    @Bean
    public RSAGateWayFilterFactory rsaGateWayFilterFactory() {
        RSAGateWayFilterFactory rsaGateWayFilterFactory = new RSAGateWayFilterFactory();
        logger.info(" create bean rsaGateWayFilterFactory");
        return rsaGateWayFilterFactory;
    }

    @Bean
    public SunshineRouteDefinitionRepository routeDefinitionRepository(GatewayProperties gatewayProperties, RouteService routeService) {
        SunshineRouteDefinitionRepository sunshineRouteDefinitionRepository = new SunshineRouteDefinitionRepository(gatewayProperties, routeService);
        logger.info(" create bean sunshineRouteDefinitionRepository");
        return sunshineRouteDefinitionRepository;
    }

    @Bean
    public GlobalFilter customFilter() {
        SunshineGlobalFilter sunshineGlobalFilter = new SunshineGlobalFilter();
        logger.info(" create bean sunshineGlobalFilter");
        return sunshineGlobalFilter;
    }

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

    //    @Bean
    public HttpsClientRequestFactory httpsClientRequestFactory() {
        return new HttpsClientRequestFactory();
    }
//    https://blog.csdn.net/Chipslyc/article/details/98851831?spm=1001.2101.3001.6650.17&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-17-98851831-blog-112706944.pc_relevant_antiscanv2&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-17-98851831-blog-112706944.pc_relevant_antiscanv2&utm_relevant_index=27
}
