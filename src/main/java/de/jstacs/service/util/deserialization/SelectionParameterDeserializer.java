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

    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public SelectionParameter deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode jsonNode = objectMapper.readTree(jsonParser);
        JsonNode nameNode = jsonNode.get("name");
        JsonNode dataTypeNode = jsonNode.get("dataType");
        JsonNode commentNode = jsonNode.get("comment");
        JsonNode requiredNode = jsonNode.get("required");
        JsonNode selectedNameNode = jsonNode.get("selectedName");
        JsonNode parametersInCollectionNode = jsonNode.get("parametersInCollection");

        String name = nameNode.textValue();
        String dataType = dataTypeNode.textValue();
        String comment = commentNode.textValue();
        boolean required = requiredNode.booleanValue();
        String selectedName = selectedNameNode.textValue();

        SimpleParameterSet simpleParameterSet = objectMapper.readValue(parametersInCollectionNode.toString(),
                SimpleParameterSet.class);

        Object[] values = selectValues(simpleParameterSet);
        String[] keys = selectKeys(simpleParameterSet);

        try {
            SelectionParameter selectionParameter = new SelectionParameter(DataType.valueOf(dataType), keys, values,
                    name, comment, required);
            selectionParameter.setValue(selectedName);
            return selectionParameter;
        } catch (IllegalValueException | InconsistentCollectionException | DatatypeNotValidException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

    // TODO: Use Map<String, Object> and only one iteration ;-)
    private Object[] selectValues(ParameterSet parameterSet) {
        List<Object> values = new ArrayList<Object>();
        int numberOfParameters = parameterSet.getNumberOfParameters();
        for (int i = 0; i < numberOfParameters; i++) {
            Parameter parameter = parameterSet.getParameterAt(i);
            Object valueAtI = parameter.getValue();
            values.add(valueAtI);
        }
        return values.toArray();
    }

    private String[] selectKeys(ParameterSet parameterSet) {
        List<String> keys = new ArrayList<String>();
        int numberOfParameters = parameterSet.getNumberOfParameters();
        for (int i = 0; i < numberOfParameters; i++) {
            Parameter parameter = parameterSet.getParameterAt(i);
            String valueAtI = parameter.getName();
            keys.add(valueAtI);
        }
        return keys.toArray(new String[] {});
    }

}
