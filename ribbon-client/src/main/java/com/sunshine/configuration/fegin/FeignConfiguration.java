package com.sunshine.configuration.fegin;

import feign.Client;
import feign.Feign;
import feign.Logger;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class FeignConfiguration {



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
//    @ConditionalOnMissingBean
 /*   public Client feignClient() {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            return new Client.Default(ctx.getSocketFactory(), (hostname, session) -> true);
        } catch (Exception e) {
            return null;
        }
    }*/

/*//    @Bean
    public Feign.Builder feignBuilder() {
        final Client trustSSLSockets = client();
        return Feign.builder().client(trustSSLSockets);
    }*/


//    @Bean
    public Client client() throws Exception {
        Client client= new Client.Default(getFeignTrustingSSLSocketFactory(), new NoopHostnameVerifier());
        return client;
    }

    public   SSLSocketFactory getFeignTrustingSSLSocketFactory() throws Exception {
        // 密码
        String passwd = "sunshine";
        String keyStoreType = "PKCS12";
        // 读取证书
        InputStream inputStream = null;
        SSLContext sslContext = SSLContext.getInstance("SSL");
        try {
            // 加载证书地址
            inputStream = FeignConfiguration.class.getResourceAsStream("/sunshine.p12");
            // 加密密钥和证书的存储工具 对象
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(inputStream, passwd.toCharArray());
            sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, passwd.toCharArray()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sslContext.getSocketFactory();
    }
}
