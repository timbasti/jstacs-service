package de.jstacs.service.tasks;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.service.storage.StorageProperties;
import de.jstacs.tools.ToolResult;

@Service
public class ToolResultProcessTaskFactory {

    private final String rootLocation;

    private final String resultsLocation;

    @Autowired
    public ToolResultProcessTaskFactory(StorageProperties properties) {
        this.rootLocation = properties.getRootLocation();
        this.resultsLocation = properties.getResultsLocation();
    }

    public ToolResultProcessTask create(ToolResult toolResult, ToolExecution toolExecution) {
        String userId = toolExecution.getUser().getId();
        String toolExecutionId = toolExecution.getId();
        Path resultDirectoryPath = Paths.get(rootLocation, userId, toolExecutionId, resultsLocation);
        return new ToolResultProcessTask(toolResult, resultDirectoryPath);
    }
    
}
