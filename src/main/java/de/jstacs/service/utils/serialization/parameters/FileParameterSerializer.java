package de.jstacs.service.utils.serialization.parameters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter;

@JsonComponent
public class FileParameterSerializer extends JsonSerializer<FileParameter> {

    @Override
    public void serialize(FileParameter parameter, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", parameter.getClass().getTypeName());
        jsonGenerator.writeStringField("name", parameter.getName());
        jsonGenerator.writeStringField("comment", parameter.getComment());
        jsonGenerator.writeStringField("dataType", parameter.getDatatype().name());
        jsonGenerator.writeBooleanField("required", parameter.isRequired());
        jsonGenerator.writeStringField("acceptedMimeType", parameter.getAcceptedMimeType());
        jsonGenerator.writeObjectField("fileContents", parameter.getFileContents());
        jsonGenerator.writeEndObject();
    }

}