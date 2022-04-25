package com.sunshine.configuration.qualifier;

import com.sunshine.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QualifierConfiguration {

    @Bean
    public User user1(){
        return new User("1");
    }

    @Bean
    public User user2(){
        return new User("2");
    }

    @Bean
    @Qualifier
    public User user3(){
        return new User("3");
    }

    @Bean
    @Qualifier
    public User user4(){
        return new User("4");
    }
}
