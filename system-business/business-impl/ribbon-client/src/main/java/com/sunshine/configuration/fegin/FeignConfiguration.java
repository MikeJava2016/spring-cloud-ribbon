package com.sunshine.configuration.fegin;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.WeightedResponseTimeRule;
import feign.Client;
import feign.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequestTransformer;
import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.HttpRequestWrapper;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.net.HttpURLConnection;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class FeignConfiguration {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(FeignConfiguration.class);

    @Bean
    public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory,
                              SpringClientFactory clientFactory, okhttp3.OkHttpClient okHttpClient) {
        feign.okhttp.OkHttpClient delegate = new feign.okhttp.OkHttpClient(okHttpClient);
        logger.info("FeignConfiguration feignClient...huzhanglinFeignConfiguration ");
        return new LoadBalancerFeignClient(delegate, cachingFactory, clientFactory);
    }

    @Bean
    public OkHttpClientFactory okHttpClientFactory(OkHttpClient.Builder builder) {
        logger.info("FeignConfiguration okHttpClientFactory...huzhanglinFeignConfiguration ");
        return new DefaultOkHttpClientFactory(builder);
    }

    @Bean
    public OkHttpClientConnectionPoolFactory connPoolFactory() {
        logger.info("FeignConfiguration OkHttpClientConnectionPoolFactory...huzhanglinFeignConfiguration ");
        return new DefaultOkHttpClientConnectionPoolFactory();
    }

    @Bean
    public OkHttpClient.Builder okHttpClientBuilder() {
        logger.info("FeignConfiguration OkHttpClient.Builder...huzhanglinFeignConfiguration ");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(chain -> {
             // 打印请求超时时长大于某个时间的请求
            // 设置header
            Request originRequest = chain.request();
            Request newRequest = originRequest.newBuilder().addHeader("ribbon-client-token", "huzhanglin")
                    .build();
            // 从当前线程中获取数据  放入请求头中
            logger.info("uri = {}",originRequest.url().toString());
            return chain.proceed(newRequest);
        });
        return builder;
    }

    @Bean
    @Primary
    @ConditionalOnMissingClass("org.springframework.retry.support.RetryTemplate")
    public CachingSpringLoadBalancerFactory cachingLBClientFactory(
            SpringClientFactory factory) {
        logger.info("FeignConfiguration CachingSpringLoadBalancerFactory...huzhanglinFeignConfiguration ");
        return new CachingSpringLoadBalancerFactory(factory);
    }


    /**
     * 日志级别
     *
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }


//    @Bean
    HttpsClientRequestFactory httpsClientRequestFactory(){
        return new HttpsClientRequestFactory();
    }


    public class HttpsClientRequestFactory extends SimpleClientHttpRequestFactory {

        @Override
        protected void prepareConnection(HttpURLConnection connection, String httpMethod) {
            try {
                if (!(connection instanceof HttpsURLConnection)) {// http协议
                    //throw new RuntimeException("An instance of HttpsURLConnection is expected");
                    super.prepareConnection(connection, httpMethod);
                }
                if (connection instanceof HttpsURLConnection) {// https协议，修改协议版本
                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    // 信任任何链接
                    TrustStrategy anyTrustStrategy = new TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
                            return true;
                        }
                    };
                    SSLContext ctx = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
                    ((HttpsURLConnection) connection).setSSLSocketFactory(ctx.getSocketFactory());
                    HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
                    super.prepareConnection(httpsConnection, httpMethod);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Bean
    public IRule WeightedResponseTimeRule(){
        WeightedResponseTimeRule weightedResponseTimeRule = new WeightedResponseTimeRule();
        logger.info("RibbonConfiguration weightedResponseTimeRule ");
        return weightedResponseTimeRule;
    }

    @Bean
    public LoadBalancerRequestTransformer loadBalancerRequestTransformer(){
        logger.info("RibbonConfiguration loadBalancerRequestTransformer ");
        return (request, instance) -> {
            logger.info("RibbonConfiguration LoadBalancerRequestTransformer = {} ",request.getURI());
            HttpHeaders headers = request.getHeaders();
            HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
            requestWrapper.getHeaders().add("localB","1324");
            return requestWrapper;
        };
    }
}
