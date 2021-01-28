package de.jstacs.service.util.serialization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.Maps;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.DataType;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.ParameterSet;
import de.jstacs.parameters.SelectionParameter;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.SimpleParameterSet;
import de.jstacs.parameters.validation.NumberValidator;
import de.jstacs.parameters.validation.ParameterValidator;
import de.jstacs.parameters.validation.RegExpValidator;

@JsonComponent
public class SelectionParameterSerializer extends JsonSerializer<SelectionParameter> {

    @Override
    public void serialize(SelectionParameter parameter, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", parameter.getClass().getTypeName());
        jsonGenerator.writeStringField("name", parameter.getName());
        jsonGenerator.writeStringField("comment", parameter.getComment());
        jsonGenerator.writeStringField("dataType", parameter.getDatatype().name());
        jsonGenerator.writeStringField("errorMessage", parameter.getErrorMessage());
        jsonGenerator.writeBooleanField("required", parameter.isRequired());
        jsonGenerator.writeNumberField("selected", parameter.getSelected());
        this.writeTypeRelatedFields(parameter, jsonGenerator);
        jsonGenerator.writeEndObject();
    }

    private void writeTypeRelatedFields(SelectionParameter parameter, JsonGenerator jsonGenerator) throws IOException {
        DataType dataType = parameter.getDatatype();
        switch (dataType) {
            case PARAMETERSET:
                jsonGenerator.writeArrayFieldStart("parametersInCollection");
                ParameterSet parametersInCollection = parameter.getParametersInCollection();
                int numberOfParameters = parametersInCollection.getNumberOfParameters();
                for (int i = 0; i < numberOfParameters; i++) {
                    SimpleParameterSet parameterSet = (SimpleParameterSet) parametersInCollection.getParameterAt(i).getValue();
                    jsonGenerator.writeObject(parameterSet);
                }
                jsonGenerator.writeEndArray();
                break;
            default:
                break;
        }
    }

}