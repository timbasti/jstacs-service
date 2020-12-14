package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.parameters.SimpleParameter.IllegalValueException;

@JsonComponent
public class FileParameterDeserializer extends JsonDeserializer<FileParameter> {

    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public FileParameter deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        TreeNode treeNode = objectMapper.readTree(jsonParser);
        TextNode nameNode = (TextNode) treeNode.get("name");
        TextNode commentNode = (TextNode) treeNode.get("comment");
        BooleanNode requiredNode = (BooleanNode) treeNode.get("required");
        TextNode acceptedMimeTypeNode = (TextNode) treeNode.get("acceptedMimeType");
        ObjectNode fileContentsNode = (ObjectNode) treeNode.get("fileContents");

        FileRepresentation fileRepresentation = objectMapper.readValue(fileContentsNode.toString(),
                FileRepresentation.class);

        FileParameter fileParameter = new FileParameter(nameNode.asText(), commentNode.asText(),
                acceptedMimeTypeNode.asText(), requiredNode.asBoolean());

        try {
            fileParameter.setValue(fileRepresentation);
            return fileParameter;
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }

        return null;

    }

}
