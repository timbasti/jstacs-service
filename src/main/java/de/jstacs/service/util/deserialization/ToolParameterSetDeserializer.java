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

import de.jstacs.parameters.Parameter;
import de.jstacs.tools.ToolParameterSet;

@JsonComponent
public class ToolParameterSetDeserializer extends JsonDeserializer<ToolParameterSet> {

    @Override
    public ToolParameterSet deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        TextNode nameNode = (TextNode) treeNode.get("toolName");
        ArrayNode parametersNode = (ArrayNode) treeNode.get("parameters");

        SimpleModule module = new SimpleModule();
        ParameterDeserializer parameterDeserializer = new ParameterDeserializer();
        module.addDeserializer(Parameter.class, parameterDeserializer);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);

        Parameter[] parameters = objectMapper.readValue(parametersNode.toString(), Parameter[].class);
        ToolParameterSet toolParameterSet = new ToolParameterSet(nameNode.asText(), parameters);

        return toolParameterSet;
    }

}
