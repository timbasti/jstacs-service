package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ShortNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.DataType;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.SimpleParameter.DatatypeNotValidException;
import de.jstacs.parameters.SimpleParameter.IllegalValueException;

@JsonComponent
public class ParameterDeserializer extends JsonDeserializer<Parameter> {

    @Override
    public Parameter deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        TextNode parameterTypeNode = (TextNode) treeNode.get("type");
        switch (parameterTypeNode.asText()) {
            case "de.jstacs.parameters.SimpleParameter":
                return this.deserializeSimpleParameter(treeNode);
            default:
                return null;
        }
    }

    private SimpleParameter deserializeSimpleParameter(TreeNode treeNode) throws IOException {
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
            throws IllegalValueException {
        String fieldName = "value";
        switch (dataType) {
            case CHAR:
            case STRING:
                TextNode textNode = (TextNode) treeNode.get(fieldName);
                parameter.setValue(textNode.asText());
                break;
            case LONG:
                LongNode longNode = (LongNode) treeNode.get(fieldName);
                parameter.setValue(longNode.longValue());
                break;
            case INT:
                IntNode intNode = (IntNode) treeNode.get(fieldName);
                parameter.setValue(intNode.intValue());
                break;
            case BYTE:
            case SHORT:
                ShortNode shortNode = (ShortNode) treeNode.get(fieldName);
                parameter.setValue(shortNode.shortValue());
                break;
            case FLOAT:
                FloatNode floatNode = (FloatNode) treeNode.get(fieldName);
                parameter.setValue(floatNode.floatValue());
                break;
            case DOUBLE:
                DoubleNode doubleNode = (DoubleNode) treeNode.get(fieldName);
                parameter.setValue(doubleNode.doubleValue());
                break;
            case BOOLEAN:
                BooleanNode booleanNode = (BooleanNode) treeNode.get(fieldName);
                parameter.setValue(booleanNode.booleanValue());
                break;
            default:
                break;
        }
    }

}
