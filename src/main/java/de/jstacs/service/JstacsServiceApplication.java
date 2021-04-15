package de.jstacs.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import de.jstacs.service.storage.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class JstacsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JstacsServiceApplication.class, args);
    }

}
