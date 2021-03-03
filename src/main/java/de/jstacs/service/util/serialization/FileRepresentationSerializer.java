package de.jstacs.service.util.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.service.storage.FileSystemStorageService;

@JsonComponent
public class FileRepresentationSerializer extends JsonSerializer<FileRepresentation> {

    private final FileSystemStorageService storageService;

    @Autowired
    public FileRepresentationSerializer(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public void serialize(FileRepresentation fileRepresentation, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException {
        String fileName = fileRepresentation.getFilename();
        String relativeFilePath = storageService.relativizeFilePath(fileName);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", relativeFilePath);
        jsonGenerator.writeEndObject();
    }

}
