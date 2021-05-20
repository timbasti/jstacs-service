package de.jstacs.service.utils.deserialization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.DataType;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.ParameterSet;
import de.jstacs.parameters.SelectionParameter;
import de.jstacs.parameters.SimpleParameterSet;
import de.jstacs.parameters.AbstractSelectionParameter.InconsistentCollectionException;
import de.jstacs.parameters.SimpleParameter.DatatypeNotValidException;
import de.jstacs.parameters.SimpleParameter.IllegalValueException;

@JsonComponent
public class SelectionParameterDeserializer extends JsonDeserializer<SelectionParameter> {

    @Override
    public SelectionParameter deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode rootNode = context.readTree(jsonParser);
        JsonNode nameNode = rootNode.get("name");
        JsonNode dataTypeNode = rootNode.get("dataType");
        JsonNode commentNode = rootNode.get("comment");
        JsonNode requiredNode = rootNode.get("required");
        JsonNode selectedNameNode = rootNode.get("selected");
        JsonNode parametersInCollectionNode = rootNode.get("parametersInCollection");

        JsonParser parametersInCollectionJsonParser = parametersInCollectionNode.traverse();
        parametersInCollectionJsonParser.nextToken();

        SimpleParameterSet simpleParameterSet = context.readValue(parametersInCollectionJsonParser,
                SimpleParameterSet.class);

        Map<String, Object> keysAndValues = this.selectKeysAndValues(simpleParameterSet);
        Object[] values = keysAndValues.values().toArray();
        String[] keys = keysAndValues.keySet().toArray(new String[] {});

        try {
            SelectionParameter selectionParameter = new SelectionParameter(DataType.valueOf(dataTypeNode.textValue()),
                    keys, values, nameNode.textValue(), commentNode.textValue(), requiredNode.booleanValue());
            selectionParameter.setValue(selectedNameNode.textValue());
            return selectionParameter;
        } catch (IllegalValueException | InconsistentCollectionException | DatatypeNotValidException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

    private Map<String, Object> selectKeysAndValues(ParameterSet parameterSet) {
        Map<String, Object> keysAndValues = new HashMap<String, Object>();
        int numberOfParameters = parameterSet.getNumberOfParameters();
        for (int i = 0; i < numberOfParameters; i++) {
            Parameter parameter = parameterSet.getParameterAt(i);
            String key = parameter.getName();
            Object value = parameter.getValue();
            keysAndValues.put(key, value);
        }
        return keysAndValues;
    }

}
