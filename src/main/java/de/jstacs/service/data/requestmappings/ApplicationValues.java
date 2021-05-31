package de.jstacs.service.data.requestmappings;

import java.util.LinkedHashSet;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ApplicationValues {

    @NonNull
    private String name;

    @NonNull
    private LinkedHashSet<Long> toolIds;

}
