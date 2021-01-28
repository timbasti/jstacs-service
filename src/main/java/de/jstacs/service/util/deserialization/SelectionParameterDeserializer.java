package de.jstacs.service.util.deserialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.DataType;
import de.jstacs.parameters.SelectionParameter;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.SimpleParameterSet;
import de.jstacs.parameters.AbstractSelectionParameter.InconsistentCollectionException;
import de.jstacs.parameters.SimpleParameter.DatatypeNotValidException;
import de.jstacs.parameters.SimpleParameter.IllegalValueException;
import de.jstacs.parameters.validation.NumberValidator;
import de.jstacs.parameters.validation.RegExpValidator;

@JsonComponent
public class SelectionParameterDeserializer extends JsonDeserializer<SelectionParameter> {

    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /*
     * comment: "select something" dataType: "PARAMETERSET" errorMessage: null name:
     * "selection" parametersInCollection: [{type:
     * "de.jstacs.parameters.SimpleParameterSet", errorMessage: null, parameters:
     * [,…]},…] required: true type: "de.jstacs.parameters.SelectionParameter"
     * value: "0"
     */

    @Override
    public SelectionParameter deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode jsonNode = objectMapper.readTree(jsonParser);
        JsonNode dataTypeNode = jsonNode.get("dataType");
        JsonNode nameNode = jsonNode.get("name");
        JsonNode commentNode = jsonNode.get("comment");
        JsonNode requiredNode = jsonNode.get("required");
        JsonNode valueNode = jsonNode.get("value");
        ArrayNode parametersInCollectionNode = (ArrayNode) jsonNode.get("parametersInCollection");

        SimpleParameterSet[] parameters = objectMapper.readValue(parametersInCollectionNode.toString(),
                SimpleParameterSet[].class);

        List<String> selectionKeys = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            selectionKeys.add(nameNode.asText() + '-' + i);
        }

        try {
            SelectionParameter selectionParameter = new SelectionParameter(DataType.valueOf(dataTypeNode.asText()),
                    selectionKeys.toArray(new String[0]), parameters, nameNode.asText(), commentNode.asText(),
                    requiredNode.asBoolean());
            selectionParameter.setValue(nameNode.asText() + '-' + valueNode.asText());
            return selectionParameter;
        } catch (InconsistentCollectionException | IllegalValueException | DatatypeNotValidException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

}
