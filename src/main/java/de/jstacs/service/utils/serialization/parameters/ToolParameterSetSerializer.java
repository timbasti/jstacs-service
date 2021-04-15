package de.jstacs.service.utils.serialization.parameters;

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
        jsonGenerator.writeStartArray();
        int numberOfParameters = parameterSet.getNumberOfParameters();
        for (int i = 0; i < numberOfParameters; i++) {
            Parameter parameter = parameterSet.getParameterAt(i);
            jsonGenerator.writeObject(parameter);
        }
        jsonGenerator.writeEndArray();
    }
    
}
