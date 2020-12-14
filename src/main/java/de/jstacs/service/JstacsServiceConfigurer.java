package de.jstacs.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JstacsServiceConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = new String[] { "http://localhost:3000", "https://jstacs-online.herokuapp.com" };
        String[] headers = new String[] { "Access-Control-Allow-Headers", "Access-Control-Allow-Origin",
                "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin", "Cache-Control",
                "Content-Type" };
        registry.addMapping("/test").allowedOrigins(origins).allowedMethods("GET", "POST").allowedHeaders(headers);
        registry.addMapping("/").allowedOrigins(origins).allowedMethods("GET").allowedHeaders(headers);
    }
}
