package de.jstacs.service.data.requestmappings;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ExecutionValues {

    @NonNull
    private Long toolId;

    private String executionName = "";

    private String executionNotes = "";

}
