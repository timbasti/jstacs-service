package de.jstacs.service.utils.serialization.entities;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.service.data.entities.Tool;
import de.jstacs.service.data.entities.ToolExecution;

@JsonComponent
public class ToolExecutionSerializer extends JsonSerializer<ToolExecution> {

    @Override
    public void serialize(ToolExecution toolExecution, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        String[] results = toolExecution.getResults();
        String state = toolExecution.getState().name();
        Date createdAtDate = toolExecution.getCreatedAt();
        Tool tool = toolExecution.getTool();

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", toolExecution.getId());
        jsonGenerator.writeStringField("name", toolExecution.getName());
        jsonGenerator.writeStringField("parameterValues", toolExecution.getParameterValues());
        jsonGenerator.writeNumberField("progress", toolExecution.getProgress());
        jsonGenerator.writeStringField("protocol", toolExecution.getProtocol());
        jsonGenerator.writeFieldName("results");
        jsonGenerator.writeArray(results, 0, results.length);
        jsonGenerator.writeStringField("state", state);
        jsonGenerator.writeStringField("createdAt", DateFormat.getInstance().format(createdAtDate));
        jsonGenerator.writeStringField("notes", toolExecution.getNotes());
        jsonGenerator.writeFieldName("tool");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", tool.getId());
        jsonGenerator.writeStringField("name", tool.getName());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
    }
    
}
