package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.SimpleParameterSet;

@JsonComponent
public class SimpleParameterSetDeserializer extends JsonDeserializer<SimpleParameterSet> {
    @Override
    public SimpleParameterSet deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode rootNode = context.readTree(jsonParser);
        JsonNode parametersNode = rootNode.get("parameters");

        JsonParser parameterJsonParser = parametersNode.traverse();
        parameterJsonParser.nextToken();

        Parameter[] parameters = context.readValue(parameterJsonParser, Parameter[].class);
        SimpleParameterSet simpleParameterSet = new SimpleParameterSet(parameters);
        return simpleParameterSet;

    }

}
