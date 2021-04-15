package de.jstacs.service.utils.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.Parameter;
import de.jstacs.tools.ToolParameterSet;

@JsonComponent
public class ToolParameterSetDeserializer extends JsonDeserializer<ToolParameterSet> {

    @Override
    public ToolParameterSet deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {

        JsonNode rootNode = context.readTree(jsonParser);
        JsonNode nameNode = rootNode.get("toolName");
        JsonNode parametersNode = rootNode.get("parameters");

        JsonParser parameterJsonParser = parametersNode.traverse();
        parameterJsonParser.nextToken();

        Parameter[] parameters = context.readValue(parameterJsonParser, Parameter[].class);
        ToolParameterSet toolParameterSet = new ToolParameterSet(nameNode.textValue(), parameters);

        return toolParameterSet;

    }

}
