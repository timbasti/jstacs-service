package de.jstacs.service.utils.serialization.parameters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.DataType;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.ParameterSet;
import de.jstacs.parameters.SelectionParameter;

@JsonComponent
public class SelectionParameterSerializer extends JsonSerializer<SelectionParameter> {

    @Override
    public void serialize(SelectionParameter selectionParameter, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        int selected = selectionParameter.getSelected();
        ParameterSet parameters = selectionParameter.getParametersInCollection();
        Parameter selectedParameter = parameters.getParameterAt(selected);
        String selectedName = selectedParameter.getName();
        Object value = selectedName;

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", selectionParameter.getClass().getTypeName());
        jsonGenerator.writeStringField("name", selectionParameter.getName());
        jsonGenerator.writeStringField("comment", selectionParameter.getComment());
        jsonGenerator.writeStringField("dataType", selectionParameter.getDatatype().name());
        jsonGenerator.writeStringField("errorMessage", selectionParameter.getErrorMessage());
        jsonGenerator.writeBooleanField("required", selectionParameter.isRequired());
        jsonGenerator.writeBooleanField("isAtomic", selectionParameter.isAtomic());
        jsonGenerator.writeObjectField("value", value);
        jsonGenerator.writeObjectField("parameters", parameters);
        jsonGenerator.writeEndObject();
    }

}