package de.jstacs.service.util.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter.FileRepresentation;

@JsonComponent
public class FileRepresentationSerializer extends JsonSerializer<FileRepresentation> {

    @Override
    public void serialize(FileRepresentation fileRepresentation, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("content", fileRepresentation.getContent());
        jsonGenerator.writeStringField("fileName", fileRepresentation.getFilename());
        jsonGenerator.writeEndObject();
    }

}
