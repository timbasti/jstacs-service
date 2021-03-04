package de.jstacs.service.util.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.ParameterSetContainer;
import de.jstacs.parameters.SelectionParameter;
import de.jstacs.parameters.SimpleParameter;
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
            this.serializeParameter(parameter, jsonGenerator);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }

    private void serializeParameter(Parameter parameter, JsonGenerator jsonGenerator)
            throws IOException {
        String type = parameter.getClass().getTypeName();
        switch (type) {
            case "de.jstacs.parameters.SimpleParameter":
                jsonGenerator.writeObject((SimpleParameter) parameter);
                break;
            case "de.jstacs.parameters.FileParameter":
                jsonGenerator.writeObject((FileParameter) parameter);
                break;
            case "de.jstacs.parameters.SelectionParameter":
                jsonGenerator.writeObject((SelectionParameter) parameter);
                break;
            case "de.jstacs.parameters.ParameterSetContainer":
                jsonGenerator.writeObject((ParameterSetContainer) parameter);
                break;
            default:
                jsonGenerator.writeNull();
                break;
        }
    }
    
}
