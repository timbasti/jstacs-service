package de.jstacs.service.utils.serialization.entities;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.service.data.entities.ToolExecution;

@JsonComponent
public class ToolExecutionSerializer extends JsonSerializer<ToolExecution> {

    @Override
    public void serialize(ToolExecution toolExecution, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        String executionId = toolExecution.getId();
        String name = toolExecution.getName();
        String parameterValues = toolExecution.getParameterValues();
        Double progress = toolExecution.getProgress();
        String protocol = toolExecution.getProtocol();
        String[] results = toolExecution.getResults();
        String state = toolExecution.getState().name();
        Date createdAtDate = toolExecution.getCreatedAt();
        String notes = toolExecution.getNotes();

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", executionId);
        jsonGenerator.writeStringField("name", name);
        jsonGenerator.writeStringField("parameterValues", parameterValues);
        jsonGenerator.writeNumberField("progress", progress);
        jsonGenerator.writeStringField("protocol", protocol);
        jsonGenerator.writeFieldName("results");
        jsonGenerator.writeArray(results, 0, results.length);
        jsonGenerator.writeStringField("state", state);
        jsonGenerator.writeStringField("createdAt", DateFormat.getInstance().format(createdAtDate));
        jsonGenerator.writeStringField("notes", notes);
        jsonGenerator.writeEndObject();
    }
    
}
