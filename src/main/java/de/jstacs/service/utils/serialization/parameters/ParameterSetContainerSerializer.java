package de.jstacs.service.utils.serialization.parameters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.ParameterSetContainer;

@JsonComponent
public class ParameterSetContainerSerializer extends JsonSerializer<ParameterSetContainer> {

    @Override
    public void serialize(ParameterSetContainer parameterSetContainer, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", parameterSetContainer.getClass().getTypeName());
        jsonGenerator.writeStringField("name", parameterSetContainer.getName());
        jsonGenerator.writeStringField("comment", parameterSetContainer.getComment());
        jsonGenerator.writeStringField("dataType", parameterSetContainer.getDatatype().name());
        jsonGenerator.writeBooleanField("required", parameterSetContainer.isRequired());
        jsonGenerator.writeBooleanField("isAtomic", parameterSetContainer.isAtomic());
        jsonGenerator.writeObjectField("parameters", parameterSetContainer.getValue());
        jsonGenerator.writeEndObject();
    }

}