package de.jstacs.service.util.deserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.springframework.boot.jackson.JsonComponent;

import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.tools.ToolParameterSet;

@JsonComponent
public class ToolParameterSetDeserializer extends JsonDeserializer<ToolParameterSet> {

    @Override
    public ToolParameterSet deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        ParameterDeserializer parameterDeserializer = new ParameterDeserializer();
        SimpleParameterDeserializer simpleParameterDeserializer = new SimpleParameterDeserializer();
        FileParameterDeserializer fileParameterDeserializer = new FileParameterDeserializer();
        FileReprensentationDeserializer fileReprensentationDeserializer = new FileReprensentationDeserializer();

        module.addDeserializer(Parameter.class, parameterDeserializer);
        module.addDeserializer(SimpleParameter.class, simpleParameterDeserializer);
        module.addDeserializer(FileParameter.class, fileParameterDeserializer);
        module.addDeserializer(FileRepresentation.class, fileReprensentationDeserializer);

        objectMapper.registerModule(module);

        parameterDeserializer.setObjectMapper(objectMapper);
        simpleParameterDeserializer.setObjectMapper(objectMapper);
        fileParameterDeserializer.setObjectMapper(objectMapper);
        fileReprensentationDeserializer.setObjectMapper(objectMapper);

        TreeNode treeNode = objectMapper.readTree(jsonParser);
        TextNode nameNode = (TextNode) treeNode.get("toolName");
        ArrayNode parametersNode = (ArrayNode) treeNode.get("parameters");

        Parameter[] parameters = objectMapper.readValue(parametersNode.toString(), Parameter[].class);
        ToolParameterSet toolParameterSet = new ToolParameterSet(nameNode.asText(), parameters);

        return toolParameterSet;
    }

}
