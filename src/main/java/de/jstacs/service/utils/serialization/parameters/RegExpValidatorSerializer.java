package de.jstacs.service.utils.serialization.parameters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.validation.RegExpValidator;

@JsonComponent
public class RegExpValidatorSerializer extends JsonSerializer<RegExpValidator> {

    @Override
    public void serialize(RegExpValidator validator, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException {
        String regExp = validator.getRegExp();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("regExp", regExp);
        jsonGenerator.writeEndObject();
    }

}
