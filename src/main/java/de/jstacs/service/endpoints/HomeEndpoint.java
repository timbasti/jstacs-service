package de.jstacs.service.endpoints;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5000", "https://jstacs-online.herokuapp.com" })
@RestController
@RequestMapping
public class HomeEndpoint {

    @GetMapping
    public Map<String, String> getHome() throws IOException {
        Map<String, String> serviceProperties = new HashMap<String, String>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("service.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        String serviceVersion = (String) properties.get("version");
        String serviceName = (String) properties.get("name");
        serviceProperties.put("serviceName", serviceName);
        serviceProperties.put("serviceVersion", serviceVersion);
        return serviceProperties;
    }

}
