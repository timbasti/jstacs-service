package de.jstacs.service.data.responsemappings;

import de.jstacs.service.data.entities.Tool;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ToolOverview {
    
    @NonNull
    private Long id;

    @NonNull
    private String type;

    @NonNull
    private String name;

    public ToolOverview(Tool tool) {
        this.name = tool.getName();
        this.type = tool.getType();
        this.id = tool.getId();
    }

}
