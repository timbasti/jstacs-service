package de.jstacs.service.utils.serialization.parameters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.ParameterSet;
import de.jstacs.parameters.SelectionParameter;

@JsonComponent
public class SelectionParameterSerializer extends JsonSerializer<SelectionParameter> {

    @Override
    public void serialize(SelectionParameter selectionParameter, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        int selected = selectionParameter.getSelected();
        ParameterSet parametersInCollection = selectionParameter.getParametersInCollection();
        Parameter selectedParameter = parametersInCollection.getParameterAt(selected);
        String selectedName = selectedParameter.getName();

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", selectionParameter.getClass().getTypeName());
        jsonGenerator.writeStringField("name", selectionParameter.getName());
        jsonGenerator.writeStringField("comment", selectionParameter.getComment());
        jsonGenerator.writeStringField("dataType", selectionParameter.getDatatype().name());
        jsonGenerator.writeStringField("errorMessage", selectionParameter.getErrorMessage());
        jsonGenerator.writeBooleanField("required", selectionParameter.isRequired());
        jsonGenerator.writeBooleanField("isAtomic", selectionParameter.isAtomic());
        jsonGenerator.writeStringField("selectedName", selectedName);
        jsonGenerator.writeObjectField("parametersInCollection", parametersInCollection);
        jsonGenerator.writeEndObject();
    }

}