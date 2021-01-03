package com.qiufeng.config;

import com.qiufeng.interceptor.CommonInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

@SpringBootConfiguration
public class MySpringMVCConfig extends WebMvcConfigurerAdapter {

    @Resource
    private CommonInterceptor commonInterceptor;

    @Resource
    MyBlogConfig myBlogConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //设置用户头像映射路劲
        registry.addResourceHandler("/static/**").addResourceLocations("file:///" + myBlogConfig.getProfile() + "/avatar/");
    }
}
