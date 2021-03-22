package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.service.storage.FileSystemStorageService;

@JsonComponent
public class FileReprensentationDeserializer extends JsonDeserializer<FileRepresentation> {

    private final FileSystemStorageService storageService;

    @Autowired
    public FileReprensentationDeserializer(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public FileRepresentation deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode rootNode = context.readTree(jsonParser);
        JsonNode fileNameNode = rootNode.get("name");
        String fileName = fileNameNode.textValue();
        String absoluteFilePath = storageService.resolveFilePath(fileName);
        return new FileRepresentation(absoluteFilePath);

    }

}
