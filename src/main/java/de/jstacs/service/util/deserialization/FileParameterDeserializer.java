package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.parameters.SimpleParameter.IllegalValueException;

@JsonComponent
public class FileParameterDeserializer extends JsonDeserializer<FileParameter> {

    @Override
    public FileParameter deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        ObjectNode rootNode = (ObjectNode) context.readTree(jsonParser);
        TextNode nameNode = (TextNode) rootNode.get("name");
        TextNode commentNode = (TextNode) rootNode.get("comment");
        BooleanNode requiredNode = (BooleanNode) rootNode.get("required");
        TextNode acceptedMimeTypeNode = (TextNode) rootNode.get("acceptedMimeType");
        ObjectNode fileContentsNode = (ObjectNode) rootNode.get("fileContents");

        JsonParser fileContentsJsonParser = fileContentsNode.traverse();
        fileContentsJsonParser.nextToken();

        FileRepresentation fileRepresentation = context.readValue(fileContentsJsonParser,
                FileRepresentation.class);

        FileParameter fileParameter = new FileParameter(nameNode.textValue(), commentNode.textValue(),
                acceptedMimeTypeNode.textValue(), requiredNode.asBoolean());

        try {
            fileParameter.setValue(fileRepresentation);
            return fileParameter;
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }

        return null;

    }

}
