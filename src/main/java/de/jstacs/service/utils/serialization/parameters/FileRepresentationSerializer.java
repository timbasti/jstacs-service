package de.jstacs.service.utils.serialization.parameters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.service.storage.StorageService;
import lombok.RequiredArgsConstructor;

@JsonComponent
@RequiredArgsConstructor
public class FileRepresentationSerializer extends JsonSerializer<FileRepresentation> {

    private final StorageService storageService;

    @Override
    public void serialize(FileRepresentation fileRepresentation, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException {
        String fileName = fileRepresentation.getFilename();
        String relativeFilePath = storageService.locate(fileName).toString();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", relativeFilePath);
        jsonGenerator.writeEndObject();
    }

}
