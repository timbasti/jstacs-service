package de.jstacs.service.utils.serialization.parameters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.DataType;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.validation.NumberValidator;
import de.jstacs.parameters.validation.ParameterValidator;
import de.jstacs.parameters.validation.RegExpValidator;

@JsonComponent
public class SimpleParameterSerializer extends JsonSerializer<SimpleParameter> {

    @Override
    public void serialize(SimpleParameter parameter, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", parameter.getClass().getTypeName());
        jsonGenerator.writeStringField("name", parameter.getName());
        jsonGenerator.writeStringField("comment", parameter.getComment());
        jsonGenerator.writeStringField("dataType", parameter.getDatatype().name());
        jsonGenerator.writeStringField("errorMessage", parameter.getErrorMessage());
        jsonGenerator.writeBooleanField("required", parameter.isRequired());
        jsonGenerator.writeBooleanField("isAtomic", parameter.isAtomic());
        this.writeTypeRelatedFields(parameter, jsonGenerator);
        jsonGenerator.writeEndObject();
    }

    private void writeTypeRelatedFields(SimpleParameter parameter, JsonGenerator jsonGenerator) throws IOException {
        DataType dataType = parameter.getDatatype();
        Object value = parameter.getValue();
        String valueFieldName = "value";
        ParameterValidator validator = parameter.getValidator();
        String validatorFieldName = "validator";
        switch (dataType) {
            case CHAR:
            case STRING:
                jsonGenerator.writeObjectField(valueFieldName, value);
                jsonGenerator.writeObjectField(validatorFieldName, ((RegExpValidator) validator));
                break;
            case BYTE:
            case SHORT:
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
                jsonGenerator.writeObjectField(valueFieldName, value);
                jsonGenerator.writeObjectField(validatorFieldName, ((NumberValidator<?>) validator));
                break;
            case BOOLEAN:
                jsonGenerator.writeObjectField(valueFieldName, value);
                break;
            default:
                jsonGenerator.writeNullField(valueFieldName);
                break;
        }
    }

}