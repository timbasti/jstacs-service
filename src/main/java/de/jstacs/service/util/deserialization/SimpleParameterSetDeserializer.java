package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.SimpleParameterSet;

@JsonComponent
public class SimpleParameterSetDeserializer extends JsonDeserializer<SimpleParameterSet> {
    @Override
    public SimpleParameterSet deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        ObjectNode rootNode = (ObjectNode) context.readTree(jsonParser);
        ArrayNode parametersNode = (ArrayNode) rootNode.get("parameters");
        
        JsonParser parameterJsonParser = parametersNode.traverse();
        parameterJsonParser.nextToken();

        Parameter[] parameters = context.readValue(parameterJsonParser, Parameter[].class);
        SimpleParameterSet simpleParameterSet = new SimpleParameterSet(parameters);
        return simpleParameterSet;

    }

}
