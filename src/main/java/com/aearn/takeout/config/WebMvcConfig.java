package com.aearn.takeout.config;

import com.aearn.takeout.common.JacksonObjectMapper;
import com.aearn.takeout.filter.LoginCheckFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport{
    //设置静态资源映射
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
        log.info("开始进行静态资源映射");
    }
    //扩展MVC框架的消息转换器
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("拓展消息转换器..");
        //创建消息转换器
        //消息转换器的作用是将controller的返回结果转成相应的json格式,再转换到前端页面。
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器,底层使用json将java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器追加到mvc的框架转换器集合中
        converters.add(0,messageConverter);
    }


}
