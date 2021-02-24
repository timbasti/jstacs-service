package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.ParameterSetContainer;
import de.jstacs.parameters.SimpleParameterSet;

@JsonComponent
public class ParameterSetContainerDeserializer extends JsonDeserializer<ParameterSetContainer> {

    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ParameterSetContainer deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode jsonNode = objectMapper.readTree(jsonParser);
        JsonNode nameNode = jsonNode.get("name");
        JsonNode commentNode = jsonNode.get("comment");
        JsonNode valueNode = jsonNode.get("value");

        SimpleParameterSet parameterSet = objectMapper.readValue(valueNode.toString(), SimpleParameterSet.class);

        ParameterSetContainer parameterSetContainer = new ParameterSetContainer(nameNode.textValue(),
                commentNode.textValue(), parameterSet);

        return parameterSetContainer;
    }

}
