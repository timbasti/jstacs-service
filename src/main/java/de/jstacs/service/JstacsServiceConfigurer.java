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
        registry.addMapping("/files").allowedOrigins(origins).allowedMethods("OPTIONS", "HEAD", "POST")
                .allowedHeaders(headers);
        registry.addMapping("/files/{filename:.+}").allowedOrigins(origins).allowedMethods("OPTIONS", "HEAD", "GET")
                .allowedHeaders(headers);
        registry.addMapping("/files/results/{filename:.+}").allowedOrigins(origins).allowedMethods("OPTIONS", "HEAD", "GET")
                .allowedHeaders(headers);
        registry.addMapping("/test").allowedOrigins(origins).allowedMethods("OPTIONS", "HEAD", "GET", "POST")
                .allowedHeaders(headers);
        registry.addMapping("/tools").allowedOrigins(origins).allowedMethods("OPTIONS", "HEAD", "GET", "POST")
                .allowedHeaders(headers);
        registry.addMapping("/tools/{tool}").allowedOrigins(origins).allowedMethods("OPTIONS", "HEAD", "GET", "POST")
                .allowedHeaders(headers);
        registry.addMapping("/tools/{tool}/results").allowedOrigins(origins)
                .allowedMethods("OPTIONS", "HEAD", "GET", "POST").allowedHeaders(headers);
        registry.addMapping("/").allowedOrigins(origins).allowedMethods("OPTIONS", "HEAD", "GET")
                .allowedHeaders(headers);
    }

}
