package de.jstacs.service.data.responsemappings;

import de.jstacs.service.data.entities.Tool;
import de.jstacs.tools.ToolParameterSet;
import lombok.Data;
import lombok.NonNull;

@Data
public class ToolValues {

    @NonNull
    private Tool tool;

    @NonNull
    private ToolParameterSet parameters;
    
}
