package de.jstacs.service.utils.serialization.entities;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.service.data.entities.Application;
import de.jstacs.service.data.entities.Tool;

@JsonComponent
public class ApplicationSerializer extends JsonSerializer<Application>{

    @Override
    public void serialize(Application application, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        Long applicationId = application.getId();
        String applicationName = application.getName();
        List<Tool> tools = application.getTools();

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", applicationId);
        jsonGenerator.writeStringField("name", applicationName);
        jsonGenerator.writeArrayFieldStart("tools");
        for (Tool tool : tools) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("id", tool.getId());
            jsonGenerator.writeStringField("name", tool.getName());
            jsonGenerator.writeStringField("type", tool.getType());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
    
}
