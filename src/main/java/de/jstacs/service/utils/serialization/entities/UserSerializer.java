package de.jstacs.service.utils.serialization.entities;

import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.service.data.entities.User;

@JsonComponent
public class UserSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        String userId = user.getId();
        Set<ToolExecution> toolExecutions = user.getExecutions();
        
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", userId);
        jsonGenerator.writeArrayFieldStart("toolExecutions");
        for (ToolExecution toolExecution : toolExecutions) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("id", toolExecution.getId());
            jsonGenerator.writeStringField("name", toolExecution.getName());
            jsonGenerator.writeStringField("state", toolExecution.getState().name());
            jsonGenerator.writeStringField("toolName", toolExecution.getTool().getName());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
    
}
