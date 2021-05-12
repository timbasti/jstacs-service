package de.jstacs.service.utils.serialization.entities;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.service.data.entities.Tool;

@JsonComponent
public class ToolSerializer extends JsonSerializer<Tool> {

    @Override
    public void serialize(Tool tool, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        String toolDescription = tool.getDescription();
        Long toolId = tool.getId();
        String toolName = tool.getName();
        String toolShortName = tool.getShortName();
        String toolType = tool.getType();
        String toolVersion = tool.getVersion();
        String[] toolReferences = tool.getReferences();
        String toolHelpText = tool.getHelpText();
        
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", toolId);
        jsonGenerator.writeStringField("name", toolName);
        jsonGenerator.writeStringField("shortName", toolShortName);
        jsonGenerator.writeStringField("type", toolType);
        jsonGenerator.writeStringField("version", toolVersion);
        jsonGenerator.writeStringField("helpText", toolHelpText);
        jsonGenerator.writeStringField("description", toolDescription);
        jsonGenerator.writeFieldName("references");
        jsonGenerator.writeArray(toolReferences, 0, toolReferences.length);
        jsonGenerator.writeEndObject();
    }
    
}
