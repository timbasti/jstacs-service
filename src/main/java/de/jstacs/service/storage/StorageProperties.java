package de.jstacs.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@ConfigurationProperties("file.storage")
public class StorageProperties {

    /**
     * Root folder location for storing files
     */
    private String rootLocation;

    /**
     * Folder location for storing results
     */
    private String resultsLocation;

    /**
     * Folder location for storing defaults
     */
    private String defaultsLocation;

    /**
     * Folder location for storing input files
     */
    private String inputsLocation;
}
