package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.SimpleParameterSet;

@JsonComponent
public class SimpleParameterSetDeserializer extends JsonDeserializer<SimpleParameterSet> {

    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public SimpleParameterSet deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode jsonNode = objectMapper.readTree(jsonParser);
        ArrayNode parametersNode = (ArrayNode) jsonNode.get("parameters");

        Parameter[] parameters = objectMapper.readValue(parametersNode.toString(), SimpleParameter[].class);
        SimpleParameterSet simpleParameterSet = new SimpleParameterSet(parameters);

        return simpleParameterSet;
    }

}
