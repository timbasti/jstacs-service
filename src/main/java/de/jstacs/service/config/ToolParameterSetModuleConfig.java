package de.jstacs.service.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.Module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.ParameterSetContainer;
import de.jstacs.parameters.SelectionParameter;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.SimpleParameterSet;
import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.service.storage.FileSystemStorageService;
import de.jstacs.service.util.deserialization.FileParameterDeserializer;
import de.jstacs.service.util.deserialization.FileReprensentationDeserializer;
import de.jstacs.service.util.deserialization.ParameterDeserializer;
import de.jstacs.service.util.deserialization.ParameterSetContainerDeserializer;
import de.jstacs.service.util.deserialization.SelectionParameterDeserializer;
import de.jstacs.service.util.deserialization.SimpleParameterDeserializer;
import de.jstacs.service.util.deserialization.SimpleParameterSetDeserializer;
import de.jstacs.service.util.deserialization.ToolParameterSetDeserializer;
import de.jstacs.tools.ToolParameterSet;

@Configuration
public class ToolParameterSetModuleConfig {

    private final FileSystemStorageService storageService;

    @Autowired
    public ToolParameterSetModuleConfig(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    @Bean
    public Module toolParameterSetModule() {
        SimpleModule module = new SimpleModule();

        this.addDeserializers(module);

        return module;
    }

    private void addDeserializers(SimpleModule module) {
        ParameterDeserializer parameterDeserializer = new ParameterDeserializer();
        SimpleParameterDeserializer simpleParameterDeserializer = new SimpleParameterDeserializer();
        FileParameterDeserializer fileParameterDeserializer = new FileParameterDeserializer();
        FileReprensentationDeserializer fileReprensentationDeserializer = new FileReprensentationDeserializer(storageService);
        SelectionParameterDeserializer selectionParameterDeserializer = new SelectionParameterDeserializer();
        ParameterSetContainerDeserializer parameterSetContainerDeserializer = new ParameterSetContainerDeserializer();
        ToolParameterSetDeserializer toolParameterSetDeserializer = new ToolParameterSetDeserializer();
        SimpleParameterSetDeserializer simpleParameterSetDeserializer = new SimpleParameterSetDeserializer();

        module.addDeserializer(Parameter.class, parameterDeserializer);
        module.addDeserializer(SimpleParameter.class, simpleParameterDeserializer);
        module.addDeserializer(FileParameter.class, fileParameterDeserializer);
        module.addDeserializer(FileRepresentation.class, fileReprensentationDeserializer);
        module.addDeserializer(SelectionParameter.class, selectionParameterDeserializer);
        module.addDeserializer(ParameterSetContainer.class, parameterSetContainerDeserializer);
        module.addDeserializer(ToolParameterSet.class, toolParameterSetDeserializer);
        module.addDeserializer(SimpleParameterSet.class, simpleParameterSetDeserializer);
    }

}
