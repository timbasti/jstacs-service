package de.jstacs.service.utils.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.service.storage.StorageService;
import lombok.RequiredArgsConstructor;

@JsonComponent
@RequiredArgsConstructor
public class FileReprensentationDeserializer extends JsonDeserializer<FileRepresentation> {

    private final StorageService storageService;

    @Override
    public FileRepresentation deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode rootNode = context.readTree(jsonParser);
        JsonNode fileNameNode = rootNode.get("name");
        String fileName = fileNameNode.textValue();
        String absoluteFilePath = storageService.load(fileName).toAbsolutePath().toString();
        return new FileRepresentation(absoluteFilePath);

    }

}
