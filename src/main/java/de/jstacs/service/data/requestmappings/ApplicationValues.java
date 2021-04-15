package de.jstacs.service.data.requestmappings;

import java.util.Set;

import lombok.Data;
import lombok.NonNull;

@Data
public class ApplicationValues {

    @NonNull
    private String name;

    @NonNull
    private Set<String> toolTypes;
    
}
