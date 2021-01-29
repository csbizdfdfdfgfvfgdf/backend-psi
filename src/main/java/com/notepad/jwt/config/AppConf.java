package com.notepad.jwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConf implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4202","http://ppssii.com","https://localhost:4200","http://localhost:4200","http://localhost:4201","https://192.168.0.1:8080/ws")
                .allowedMethods("GET","PUT", "DELETE","POST");
    }
}