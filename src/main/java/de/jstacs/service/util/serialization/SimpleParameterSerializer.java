package de.jstacs.service.util.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.DataType;
import de.jstacs.parameters.SimpleParameter;

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
        this.writeParameterValue(parameter.getDatatype(), parameter.getValue(), jsonGenerator);
        jsonGenerator.writeEndObject();
    }

    private void writeParameterValue(DataType dataType, Object value, JsonGenerator jsonGenerator) throws IOException {
        String fieldName = "value";
        switch (dataType) {
            case CHAR:
                jsonGenerator.writeStringField(fieldName, ((Character) value).toString());
                break;
            case STRING:
                jsonGenerator.writeStringField(fieldName, (String) value);
                break;
            case LONG:
                jsonGenerator.writeNumberField(fieldName, (Long) value);
                break;
            case INT:
                jsonGenerator.writeNumberField(fieldName, (Integer) value);
                break;
            case BYTE:
                jsonGenerator.writeNumberField(fieldName, (Byte) value);
                break;
            case SHORT:
                jsonGenerator.writeNumberField(fieldName, (Short) value);
                break;
            case FLOAT:
                jsonGenerator.writeNumberField(fieldName, (Float) value);
                break;
            case DOUBLE:
                jsonGenerator.writeNumberField(fieldName, (Double) value);
                break;
            case BOOLEAN:
                jsonGenerator.writeBooleanField(fieldName, (Boolean) value);
                break;
            default:
                jsonGenerator.writeNullField(fieldName);
                break;
        }
    }

}