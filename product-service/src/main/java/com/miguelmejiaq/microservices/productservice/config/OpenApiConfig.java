package com.miguelmejiaq.microservices.productservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OpenApiConfig implements WebMvcConfigurer{
    @Override
    public void addViewControllers(final ViewControllerRegistry registry){
        registry.addRedirectViewController("/", "/swagger-ui/index.html");
        registry.addRedirectViewController("index.html", "/swagger-ui/index.html");
    }
}
