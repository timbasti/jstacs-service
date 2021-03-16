package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.Parameter;
import de.jstacs.tools.ToolParameterSet;

@JsonComponent
public class ToolParameterSetDeserializer extends JsonDeserializer<ToolParameterSet> {

    @Override
    public ToolParameterSet deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {

        ObjectNode rootNode = (ObjectNode) context.readTree(jsonParser);
        TextNode nameNode = (TextNode) rootNode.get("toolName");
        ArrayNode parametersNode = (ArrayNode) rootNode.get("parameters");

        JsonParser parameterJsonParser = parametersNode.traverse();
        parameterJsonParser.nextToken();

        Parameter[] parameters = context.readValue(parameterJsonParser, Parameter[].class);
        ToolParameterSet toolParameterSet = new ToolParameterSet(nameNode.textValue(), parameters);

        return toolParameterSet;

    }

}
