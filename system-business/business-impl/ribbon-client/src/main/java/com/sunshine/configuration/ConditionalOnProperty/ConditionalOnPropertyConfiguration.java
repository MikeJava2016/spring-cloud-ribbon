package com.sunshine.configuration.ConditionalOnProperty;

import com.sunshine.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "sunshine",name = "a1",havingValue = "true")
public class ConditionalOnPropertyConfiguration {

    @Bean(value = "user10")
    @Qualifier
    public User user10() {
       return new User(10L);
    }

}
