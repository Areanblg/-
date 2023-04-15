package com.aearn.takeout.config;

import com.aearn.takeout.interceptor.LoginInterception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterception(redisTemplate))
                .addPathPatterns("/**")
                .excludePathPatterns("/employee/login",
                        "/employee/logout",
                        "/backend/**",
                        "/front/**",
                        "/common/**",
                        "/user/sendMsg",
                        "/user/login",
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resources",
                        "/v2/api-docs");
    }
}

