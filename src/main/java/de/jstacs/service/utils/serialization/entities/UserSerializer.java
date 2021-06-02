package de.jstacs.service.utils.serialization.entities;

import java.io.IOException;
import java.util.List;

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
        List<ToolExecution> toolExecutions = user.getExecutions();
        
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", userId);
        jsonGenerator.writeArrayFieldStart("toolExecutions");
        for (ToolExecution toolExecution : toolExecutions) {
            jsonGenerator.writeObject(toolExecution);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
    
}
