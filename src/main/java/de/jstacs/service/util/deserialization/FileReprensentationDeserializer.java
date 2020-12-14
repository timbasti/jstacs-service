package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter.FileRepresentation;

@JsonComponent
public class FileReprensentationDeserializer extends JsonDeserializer<FileRepresentation> {

    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public FileRepresentation deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        TreeNode treeNode = objectMapper.readTree(jsonParser);
        TextNode contentNode = (TextNode) treeNode.get("content");
        TextNode fileNameNode = (TextNode) treeNode.get("fileName");

        return new FileRepresentation(contentNode.asText(), fileNameNode.asText());

    }

}
