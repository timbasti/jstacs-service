package de.jstacs.service.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Root folder location for storing files
     */
    private final String rootLocation = "files";

    /**
     * Folder location for storing results
     */
    private final String resultsLocation = "results";

    /**
     * Folder location for storing defaults
     */
    private final String defaultsLocation = "defaults";
}
