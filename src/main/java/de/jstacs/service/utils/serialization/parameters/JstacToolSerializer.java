package de.jstacs.service.utils.serialization.parameters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.tools.JstacsTool;

@JsonComponent
public class JstacToolSerializer extends JsonSerializer<JstacsTool> {

    @Override
    public void serialize(JstacsTool tool, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        String[] toolReferences = tool.getReferences();

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", tool.getClass().getTypeName());
        jsonGenerator.writeStringField("toolName", tool.getToolName());
        jsonGenerator.writeStringField("toolVersion", tool.getToolVersion());
        jsonGenerator.writeStringField("description", tool.getDescription());
        jsonGenerator.writeStringField("shortName", tool.getShortName());
        jsonGenerator.writeStringField("helpText", tool.getHelpText());

        jsonGenerator.writeFieldName("references");
        if (toolReferences == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeArray(toolReferences, 0, toolReferences.length);
        }

        jsonGenerator.writeEndObject();
    }

}