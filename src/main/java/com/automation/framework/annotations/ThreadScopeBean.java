package com.automation.framework.annotations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

@Bean
@Lazy
@Scope("browserScope")
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadScopeBean {
}
