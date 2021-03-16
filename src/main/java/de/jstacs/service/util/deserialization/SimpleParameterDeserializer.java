package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.DataType;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.SimpleParameter.DatatypeNotValidException;
import de.jstacs.parameters.SimpleParameter.IllegalValueException;
import de.jstacs.parameters.validation.NumberValidator;
import de.jstacs.parameters.validation.RegExpValidator;

@JsonComponent
public class SimpleParameterDeserializer extends JsonDeserializer<SimpleParameter> {

    @Override
    public SimpleParameter deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        ObjectNode rootNode = (ObjectNode) context.readTree(jsonParser);
        TextNode dataTypeNode = (TextNode) rootNode.get("dataType");
        TextNode nameNode = (TextNode) rootNode.get("name");
        TextNode commentNode = (TextNode) rootNode.get("comment");
        BooleanNode requiredNode = (BooleanNode) rootNode.get("required");

        try {
            SimpleParameter parameter = new SimpleParameter(DataType.valueOf(dataTypeNode.textValue()),
                    nameNode.textValue(), commentNode.textValue(), requiredNode.booleanValue());
            this.readParameterValue(DataType.valueOf(dataTypeNode.textValue()), parameter, rootNode);
            return parameter;
        } catch (DatatypeNotValidException e) {
            e.printStackTrace();
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
        return null;

    }

    private void readParameterValue(DataType dataType, SimpleParameter parameter, JsonNode treeNode)
            throws IllegalValueException, IOException {
        JsonNode valueNode = treeNode.get("value");
        JsonNode validatorNode = treeNode.get("validator");
        switch (dataType) {
        case CHAR: {
            if (!validatorNode.isNull()) {
                TextNode regExpNode = (TextNode) validatorNode.get("regExp");
                RegExpValidator validator = new RegExpValidator(regExpNode.textValue());
                parameter.setValidator(validator);
            }
            parameter.setValue(valueNode.textValue().charAt(0));
            break;
        }
        case STRING: {
            if (!validatorNode.isNull()) {
                TextNode regExpNode = (TextNode) validatorNode.get("regExp");
                RegExpValidator validator = new RegExpValidator(regExpNode.textValue());
                parameter.setValidator(validator);
            }
            parameter.setValue(((TextNode) valueNode).textValue());
            break;
        }
        case LONG: {
            if (!validatorNode.isNull()) {
                NumericNode lowerBoundNode = (NumericNode) validatorNode.get("lowerBound");
                NumericNode upperBoundNode = (NumericNode) validatorNode.get("upperBound");
                long lowerBound = lowerBoundNode.longValue();
                long upperBound = upperBoundNode.longValue();
                NumberValidator<Long> validator = new NumberValidator<Long>(lowerBound, upperBound);
                parameter.setValidator(validator);
            }
            parameter.setValue(((NumericNode) valueNode).longValue());
            break;
        }
        case INT: {
            if (!validatorNode.isNull()) {
                NumericNode lowerBoundNode = (NumericNode) validatorNode.get("lowerBound");
                NumericNode upperBoundNode = (NumericNode) validatorNode.get("upperBound");
                int lowerBound = lowerBoundNode.intValue();
                int upperBound = upperBoundNode.intValue();
                NumberValidator<Integer> validator = new NumberValidator<Integer>(lowerBound, upperBound);
                parameter.setValidator(validator);
            }
            parameter.setValue(((NumericNode) valueNode).intValue());
            break;
        }
        case SHORT: {
            if (!validatorNode.isNull()) {
                NumericNode lowerBoundNode = (NumericNode) validatorNode.get("lowerBound");
                NumericNode upperBoundNode = (NumericNode) validatorNode.get("upperBound");
                short lowerBound = lowerBoundNode.shortValue();
                short upperBound = upperBoundNode.shortValue();
                NumberValidator<Short> validator = new NumberValidator<Short>(lowerBound, upperBound);
                parameter.setValidator(validator);
            }
            parameter.setValue(((NumericNode) valueNode).shortValue());
            break;
        }
        case BYTE: {
            if (!validatorNode.isNull()) {
                NumericNode lowerBoundNode = (NumericNode) validatorNode.get("lowerBound");
                NumericNode upperBoundNode = (NumericNode) validatorNode.get("upperBound");
                byte lowerBound = (byte) lowerBoundNode.shortValue();
                byte upperBound = (byte) upperBoundNode.shortValue();
                NumberValidator<Byte> validator = new NumberValidator<Byte>(lowerBound, upperBound);
                parameter.setValidator(validator);
            }
            parameter.setValue((byte) ((NumericNode) valueNode).shortValue());
            break;
        }
        case DOUBLE: {
            if (!validatorNode.isNull()) {
                NumericNode lowerBoundNode = (NumericNode) validatorNode.get("lowerBound");
                NumericNode upperBoundNode = (NumericNode) validatorNode.get("upperBound");
                double lowerBound = lowerBoundNode.doubleValue();
                double upperBound = upperBoundNode.doubleValue();
                NumberValidator<Double> validator = new NumberValidator<Double>(lowerBound, upperBound);
                parameter.setValidator(validator);
            }
            parameter.setValue(((NumericNode) valueNode).doubleValue());
            break;
        }
        case FLOAT: {
            if (!validatorNode.isNull()) {
                NumericNode lowerBoundNode = (NumericNode) validatorNode.get("lowerBound");
                NumericNode upperBoundNode = (NumericNode) validatorNode.get("upperBound");
                float lowerBound = lowerBoundNode.floatValue();
                float upperBound = upperBoundNode.floatValue();
                NumberValidator<Float> validator = new NumberValidator<Float>(lowerBound, upperBound);
                parameter.setValidator(validator);
            }
            parameter.setValue(((NumericNode) valueNode).floatValue());
            break;
        }
        case BOOLEAN: {
            parameter.setValue(((BooleanNode) valueNode).booleanValue());
            break;
        }
        default:
            break;
        }

    }

}
