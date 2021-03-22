package de.jstacs.service.config;

public class ToolParameterSetModuleConfig {

    /* private final FileSystemStorageService storageService;

    @Autowired
    public ToolParameterSetModuleConfig(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

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
    } */

}
