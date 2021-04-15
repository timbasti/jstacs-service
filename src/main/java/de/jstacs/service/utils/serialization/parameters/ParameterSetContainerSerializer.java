package de.jstacs.service.utils.serialization.parameters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.ParameterSet;
import de.jstacs.parameters.ParameterSetContainer;
import de.jstacs.parameters.SimpleParameterSet;

@JsonComponent
public class ParameterSetContainerSerializer extends JsonSerializer<ParameterSetContainer> {

    @Override
    public void serialize(ParameterSetContainer parameterSetContainer, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        ParameterSet parameterSet = parameterSetContainer.getValue();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", parameterSetContainer.getClass().getTypeName());
        jsonGenerator.writeStringField("name", parameterSetContainer.getName());
        jsonGenerator.writeStringField("comment", parameterSetContainer.getComment());
        jsonGenerator.writeStringField("dataType", parameterSetContainer.getDatatype().name());
        jsonGenerator.writeBooleanField("required", parameterSetContainer.isRequired());
        jsonGenerator.writeBooleanField("isAtomic", parameterSetContainer.isAtomic());
        jsonGenerator.writeFieldName("value");
        this.serializeParameterSet(parameterSet, jsonGenerator);
        jsonGenerator.writeEndObject();
    }

    public void serializeParameterSet(ParameterSet parameterSet, JsonGenerator jsonGenerator)
            throws IOException {
        String type = parameterSet.getClass().getTypeName();
        switch (type) {
            case "de.jstacs.parameters.SimpleParameterSet":
                jsonGenerator.writeObject((SimpleParameterSet) parameterSet);
                break;
            default:
                jsonGenerator.writeNull();
                break;
        }
    }

}