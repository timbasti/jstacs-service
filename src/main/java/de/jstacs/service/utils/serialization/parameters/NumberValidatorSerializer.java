package de.jstacs.service.utils.serialization.parameters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.validation.NumberValidator;

@JsonComponent
public class NumberValidatorSerializer extends JsonSerializer<NumberValidator<?>> {

    @Override
    public void serialize(NumberValidator<?> validator, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException {
        Object lowerBound = validator.getLowerBound();
        Object upperBound = validator.getUpperBound();
        Map<String, Object> bounds = new HashMap<String, Object>();
        bounds.put("lowerBound", lowerBound);
        bounds.put("upperBound", upperBound);
        jsonGenerator.writeObject(bounds);
    }

}
