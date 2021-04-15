package de.jstacs.service.initializers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import de.jstacs.service.data.entities.Tool;
import de.jstacs.service.data.repositories.ToolRepository;
import de.jstacs.service.services.ToolLoader;
import de.jstacs.tools.JstacsTool;

@Component
public class AvailableToolsInitializer implements CommandLineRunner {

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private ToolLoader toolLoader;

    @Override
    public void run(String... args) throws Exception {
        List<JstacsTool> loadedTools = this.toolLoader.loadAll();
        loadedTools.forEach((loadedTool) -> {
            Optional<Tool> optionalToolEntity = this.toolRepository.findByType(loadedTool.getClass().getName());
            if (optionalToolEntity.isEmpty()) {
                Tool newToolEntity = new Tool(loadedTool);
                this.toolRepository.save(newToolEntity);
            }
        });
    }

}
