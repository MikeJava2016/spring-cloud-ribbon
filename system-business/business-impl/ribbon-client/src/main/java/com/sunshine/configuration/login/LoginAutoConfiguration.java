package com.sunshine.configuration.login;

import com.sunshine.api.feign.service.IUserAuthFeignService;
import com.sunshine.modules.login.NormalLoginProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/10 12:45
 **/
@Configuration
public class LoginAutoConfiguration {

    @Bean
    public NormalLoginProcessor normalLoginProcessor( @Lazy IUserAuthFeignService iUserAuthFeignService){
        return new NormalLoginProcessor(iUserAuthFeignService);
    }
}
