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

import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.ParameterSetContainer;
import de.jstacs.parameters.SelectionParameter;
import de.jstacs.parameters.SimpleParameter;

@JsonComponent
public class ParameterDeserializer extends JsonDeserializer<Parameter> {

    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Parameter deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode treeNode = objectMapper.readTree(jsonParser);
        TextNode parameterTypeNode = (TextNode) treeNode.get("type");

        switch (parameterTypeNode.textValue()) {
            case "de.jstacs.parameters.SimpleParameter":
                return objectMapper.readValue(treeNode.toString(), SimpleParameter.class);
            case "de.jstacs.parameters.FileParameter":
                return objectMapper.readValue(treeNode.toString(), FileParameter.class);
            case "de.jstacs.parameters.SelectionParameter":
                return objectMapper.readValue(treeNode.toString(), SelectionParameter.class);
            case "de.jstacs.parameters.ParameterSetContainer":
                return objectMapper.readValue(treeNode.toString(), ParameterSetContainer.class);
            default:
                return null;
        }

    }

}
