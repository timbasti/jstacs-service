package de.jstacs.service.utils.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.ParameterSetContainer;
import de.jstacs.parameters.SelectionParameter;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.tools.DataColumnParameter;

@JsonComponent
public class ParameterDeserializer extends JsonDeserializer<Parameter> {

    @Override
    public Parameter deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode rootNode = context.readTree(jsonParser);
        JsonNode parameterTypeNode = rootNode.get("type");

        JsonParser rootNodeJsonParser = rootNode.traverse();
        rootNodeJsonParser.nextToken();

        switch (parameterTypeNode.textValue()) {
        case "de.jstacs.parameters.SimpleParameter":
            return context.readValue(rootNodeJsonParser, SimpleParameter.class);
        case "de.jstacs.parameters.FileParameter":
            return context.readValue(rootNodeJsonParser, FileParameter.class);
        case "de.jstacs.parameters.EnumParameter":
        case "de.jstacs.parameters.SelectionParameter":
            return context.readValue(rootNodeJsonParser, SelectionParameter.class);
        case "de.jstacs.parameters.ParameterSetContainer":
            return context.readValue(rootNodeJsonParser, ParameterSetContainer.class);
        case "de.jstacs.tools.DataColumnParameter":
            return context.readValue(rootNodeJsonParser, DataColumnParameter.class);
        default:
            return null;
        }

    }

}
