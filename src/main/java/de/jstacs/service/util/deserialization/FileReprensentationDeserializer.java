package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.service.storage.FileSystemStorageService;

@JsonComponent
public class FileReprensentationDeserializer extends JsonDeserializer<FileRepresentation> {

    private ObjectMapper objectMapper;
    private final FileSystemStorageService storageService;

    public FileReprensentationDeserializer(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public FileRepresentation deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        JsonNode treeNode = objectMapper.readTree(jsonParser);
        TextNode fileNameNode = (TextNode) treeNode.get("fileName");
        String fileName = fileNameNode.textValue();
        String absoluteFilePath = storageService.resolveFilePath(fileName);
        return new FileRepresentation(absoluteFilePath);
    }

}
