package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.ParameterSetContainer;
import de.jstacs.parameters.SimpleParameterSet;

@JsonComponent
public class ParameterSetContainerDeserializer extends JsonDeserializer<ParameterSetContainer> {

    @Override
    public ParameterSetContainer deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode rootNode = context.readTree(jsonParser);
        JsonNode nameNode = rootNode.get("name");
        JsonNode commentNode = rootNode.get("comment");
        JsonNode valueNode = rootNode.get("value");

        JsonParser valuJsonParser = valueNode.traverse();
        valuJsonParser.nextToken();

        SimpleParameterSet parameterSet = context.readValue(valuJsonParser, SimpleParameterSet.class);

        ParameterSetContainer parameterSetContainer = new ParameterSetContainer(nameNode.textValue(),
                commentNode.textValue(), parameterSet);

        return parameterSetContainer;
    }

}
