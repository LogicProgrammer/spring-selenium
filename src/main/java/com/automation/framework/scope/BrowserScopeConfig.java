package com.automation.framework.scope;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrowserScopeConfig {

    @Bean
    public BrowserScopePostProcessor browserScopePostProcessor(){
        return new BrowserScopePostProcessor();
    }

}
