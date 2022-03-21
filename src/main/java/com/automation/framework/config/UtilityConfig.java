package com.automation.framework.config;

import com.automation.framework.annotations.LazyConfiguration;
import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;

@LazyConfiguration
public class UtilityConfig {

    @Bean
    public Faker faker(){
        return new Faker();
    }

}
