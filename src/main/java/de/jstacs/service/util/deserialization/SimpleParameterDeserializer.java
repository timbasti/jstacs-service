package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.DataType;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.SimpleParameter.DatatypeNotValidException;
import de.jstacs.parameters.SimpleParameter.IllegalValueException;

@JsonComponent
public class SimpleParameterDeserializer extends JsonDeserializer<SimpleParameter> {

    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public SimpleParameter deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        TreeNode treeNode = objectMapper.readTree(jsonParser);
        TextNode dataTypeNode = (TextNode) treeNode.get("dataType");
        TextNode nameNode = (TextNode) treeNode.get("name");
        TextNode commentNode = (TextNode) treeNode.get("comment");
        BooleanNode requiredNode = (BooleanNode) treeNode.get("required");
        try {
            SimpleParameter parameter = new SimpleParameter(DataType.valueOf(dataTypeNode.asText()), nameNode.asText(),
                    commentNode.asText(), requiredNode.asBoolean());
            this.readParameterValue(DataType.valueOf(dataTypeNode.asText()), parameter, treeNode);
            return parameter;
        } catch (DatatypeNotValidException e) {
            e.printStackTrace();
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
        return null;

    }

    private void readParameterValue(DataType dataType, Parameter parameter, TreeNode treeNode)
            throws IllegalValueException, IOException {

        TreeNode node = treeNode.get("value");
        JsonParser parser = node.traverse();
        parser.nextToken();
        switch (dataType) {
            case CHAR:
                parameter.setValue(parser.getTextCharacters()[0]);
                break;
            case STRING:
                parameter.setValue(parser.getText());
                break;
            case LONG:
                parameter.setValue(parser.getLongValue());
                break;
            case INT:
                parameter.setValue(parser.getIntValue());
                break;
            case BYTE:
                parameter.setValue(parser.getByteValue());
                break;
            case SHORT:
                parameter.setValue(parser.getShortValue());
                break;
            case FLOAT:
                parameter.setValue(parser.getFloatValue());
                break;
            case DOUBLE:
                parameter.setValue(parser.getDoubleValue());
                break;
            case BOOLEAN:
                parameter.setValue(parser.getBooleanValue());
                break;
            default:
                parameter.setValue(null);
                break;
        }

    }

}
