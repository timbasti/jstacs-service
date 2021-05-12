package de.jstacs.service.utils.serialization.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter;

@JsonComponent
public class FileParameterSerializer extends JsonSerializer<FileParameter> {

    @Override
    public void serialize(FileParameter parameter, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        List<String> fileTypes = Arrays.asList(parameter.getAcceptedMimeType().split(","));
        List<String> fileExtensions = new ArrayList<String>();
        fileTypes.forEach((fileType) -> {
            if (fileType.startsWith(".")) {
                fileExtensions.add(fileType);
            } else {
                fileExtensions.add("." + fileType);
            }
        });
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", parameter.getClass().getTypeName());
        jsonGenerator.writeStringField("name", parameter.getName());
        jsonGenerator.writeStringField("comment", parameter.getComment());
        jsonGenerator.writeStringField("dataType", parameter.getDatatype().name());
        jsonGenerator.writeBooleanField("required", parameter.isRequired());
        jsonGenerator.writeStringField("acceptedExtensions", String.join(",", fileExtensions));
        jsonGenerator.writeObjectField("fileContents", parameter.getFileContents());
        jsonGenerator.writeEndObject();
    }

}