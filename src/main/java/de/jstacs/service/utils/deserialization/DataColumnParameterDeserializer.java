package de.jstacs.service.utils.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.ParameterSetContainer;
import de.jstacs.parameters.SimpleParameterSet;
import de.jstacs.parameters.SimpleParameter.DatatypeNotValidException;
import de.jstacs.parameters.SimpleParameter.IllegalValueException;
import de.jstacs.tools.DataColumnParameter;

@JsonComponent
public class DataColumnParameterDeserializer extends JsonDeserializer<DataColumnParameter> {

    @Override
    public DataColumnParameter deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode rootNode = context.readTree(jsonParser);
        JsonNode nameNode = rootNode.get("name");
        JsonNode commentNode = rootNode.get("comment");
        JsonNode requiredNode = rootNode.get("required");
        JsonNode dataRefNode = rootNode.get("dataRef");
        JsonNode valueNode = rootNode.get("value");

        try {
            DataColumnParameter dataColumnParameter = new DataColumnParameter(dataRefNode.textValue(), nameNode.textValue(),
                    commentNode.textValue(), requiredNode.booleanValue(), valueNode.intValue());
            return dataColumnParameter;
        } catch (DatatypeNotValidException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalValueException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

}
