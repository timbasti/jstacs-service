package de.jstacs.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JstacsServiceConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/test").allowedOrigins("http://localhost:3000", "https://jstacs-service.herokuapp.com/",
                "https://jstacs-online.herokuapp.com/").allowedMethods("GET", "POST");
        registry.addMapping("/").allowedOrigins("http://localhost:3000", "https://jstacs-service.herokuapp.com/",
                "https://jstacs-online.herokuapp.com/").allowedMethods("GET");
    }
}
