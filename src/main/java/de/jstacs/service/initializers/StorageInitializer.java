package de.jstacs.service.initializers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import de.jstacs.service.storage.StorageService;

@Component
public class StorageInitializer implements CommandLineRunner {

    @Autowired
    private StorageService storageService; 

    @Override
    public void run(String... args) throws Exception {
        // this.storageService.deleteAll();
        // this.storageService.init();
    }
    
}
