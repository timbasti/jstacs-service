package de.jstacs.service.util.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.Parameter;
import de.jstacs.tools.ToolParameterSet;

@JsonComponent
public class ToolParameterSetSerializer extends JsonSerializer<ToolParameterSet> {

    @Override
    public void serialize(ToolParameterSet parameterSet, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", parameterSet.getClass().getTypeName());
        jsonGenerator.writeStringField("toolName", parameterSet.getToolName());
        jsonGenerator.writeStringField("errorMessage", parameterSet.getErrorMessage());
        jsonGenerator.writeBooleanField("isAtomic", parameterSet.isAtomic());
        jsonGenerator.writeArrayFieldStart("parameters");
        int numberOfParameters = parameterSet.getNumberOfParameters();
        for (int i = 0; i < numberOfParameters; i++) {
            Parameter parameter = parameterSet.getParameterAt(i);
            jsonGenerator.writeObject(parameter);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
    
}
