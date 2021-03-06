package de.jstacs.service.utils.serialization.parameters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.tools.DataColumnParameter;

@JsonComponent
public class DataColumnParameterSerializer extends JsonSerializer<DataColumnParameter> {

    @Override
    public void serialize(DataColumnParameter parameter, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", parameter.getClass().getTypeName());
        jsonGenerator.writeStringField("name", parameter.getName());
        jsonGenerator.writeStringField("comment", parameter.getComment());
        jsonGenerator.writeStringField("dataType", parameter.getDatatype().name());
        jsonGenerator.writeBooleanField("required", parameter.isRequired());
        jsonGenerator.writeStringField("dataRef", parameter.getDataRef());
        jsonGenerator.writeObjectField("value", parameter.getValue());
        jsonGenerator.writeEndObject();
    }

}