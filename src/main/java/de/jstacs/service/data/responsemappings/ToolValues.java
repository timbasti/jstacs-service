package de.jstacs.service.data.responsemappings;

import de.jstacs.service.data.entities.Tool;
import de.jstacs.tools.ToolParameterSet;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ToolValues {

    @NonNull
    private Tool tool;

    @NonNull
    private ToolParameterSet parameters;
    
}
