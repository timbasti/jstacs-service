package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.SelectionParameter;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.SimpleParameterSet;
import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.service.storage.FileSystemStorageService;
import de.jstacs.tools.ToolParameterSet;

@JsonComponent
public class ToolParameterSetDeserializer extends JsonDeserializer<ToolParameterSet> {

    private final FileSystemStorageService storageService;

    @Autowired
    public ToolParameterSetDeserializer(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public ToolParameterSet deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        ParameterDeserializer parameterDeserializer = new ParameterDeserializer();
        SimpleParameterDeserializer simpleParameterDeserializer = new SimpleParameterDeserializer();
        FileParameterDeserializer fileParameterDeserializer = new FileParameterDeserializer();
        FileReprensentationDeserializer fileReprensentationDeserializer = new FileReprensentationDeserializer(storageService);
        SimpleParameterSetDeserializer simpleParameterSetDeserializer = new SimpleParameterSetDeserializer();
        SelectionParameterDeserializer selectionParameterDeserializer = new SelectionParameterDeserializer();

        module.addDeserializer(Parameter.class, parameterDeserializer);
        module.addDeserializer(SimpleParameter.class, simpleParameterDeserializer);
        module.addDeserializer(FileParameter.class, fileParameterDeserializer);
        module.addDeserializer(FileRepresentation.class, fileReprensentationDeserializer);
        module.addDeserializer(SimpleParameterSet.class, simpleParameterSetDeserializer);
        module.addDeserializer(SelectionParameter.class, selectionParameterDeserializer);

        objectMapper.registerModule(module);

        parameterDeserializer.setObjectMapper(objectMapper);
        simpleParameterDeserializer.setObjectMapper(objectMapper);
        fileParameterDeserializer.setObjectMapper(objectMapper);
        fileReprensentationDeserializer.setObjectMapper(objectMapper);
        simpleParameterSetDeserializer.setObjectMapper(objectMapper);
        selectionParameterDeserializer.setObjectMapper(objectMapper);

        JsonNode jsonNode = objectMapper.readTree(jsonParser);
        TextNode nameNode = (TextNode) jsonNode.get("toolName");
        ArrayNode parametersNode = (ArrayNode) jsonNode.get("parameters");

        Parameter[] parameters = objectMapper.readValue(parametersNode.toString(), Parameter[].class);
        ToolParameterSet toolParameterSet = new ToolParameterSet(nameNode.asText(), parameters);

        return toolParameterSet;
    }

}
