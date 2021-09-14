package de.jstacs.service.tasks;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import de.jstacs.results.savers.ResultSaver;
import de.jstacs.results.savers.ResultSaverLibrary;
import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.service.storage.StorageService;
import de.jstacs.tools.JstacsTool;
import de.jstacs.tools.ProgressUpdater;
import de.jstacs.tools.Protocol;
import de.jstacs.tools.ToolParameterSet;
import de.jstacs.tools.ToolResult;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ToolExecutionTask implements Callable<String[]> {

    @Getter
    private final ToolParameterSet toolParameterSet;

    @Getter
    private final ToolExecution toolExecution;

    private final JstacsTool jstacsTool;

    private final Protocol protocol;

    private final ProgressUpdater progressUpdater;

    private final StorageService storageService;

    private final String resultsLocation;

    public ToolExecutionTask(final JstacsTool jstacsTool, final ToolParameterSet toolParameterSet,
            final Protocol protocol, final ProgressUpdater progressUpdater, final StorageService storageService,
            final ToolExecution toolExecution, final String resultsLocation) {
        this.jstacsTool = jstacsTool;
        this.toolParameterSet = toolParameterSet;
        this.protocol = protocol;
        this.progressUpdater = progressUpdater;
        this.storageService = storageService;
        this.toolExecution = toolExecution;
        this.resultsLocation = resultsLocation;
        log.debug("Created ToolTask for: " + jstacsTool.getToolName());
    }

    public String getToolName() {
        return this.jstacsTool.getToolVersion();
    }

    @Override
    public String[] call() throws Exception {
        log.debug("Starting execution of " + this.jstacsTool.getToolName());
        ToolResult toolResult = this.jstacsTool.run(this.toolParameterSet, this.protocol, this.progressUpdater, 1);
        ResultSaver<ToolResult> resultSaver = ResultSaverLibrary.getSaver(toolResult.getClass());

        String rootLocationFileName = this.storageService.getRootLocation().toString();
        String userId = this.toolExecution.getUser().getId();
        String toolExecutionId = this.toolExecution.getId();
        Path resultDirectoryPath = Paths.get(rootLocationFileName, userId, toolExecutionId, resultsLocation);
        Path resultsDirPath = Files.createDirectories(resultDirectoryPath);
        File resultsDir = resultsDirPath.toFile();

        File[] oldFiles = resultsDir.listFiles();
        for (File oldFile : oldFiles) {
            oldFile.delete();
        }

        resultSaver.writeOutput(toolResult, resultsDir);

        List<String> resultFiles = new ArrayList<String>();
        Files.walk(resultsDirPath).filter(Files::isRegularFile).forEach((filePath) -> {
            Path filePathInStorage = this.storageService.locate(filePath.toString());
            resultFiles.add(filePathInStorage.toString());
        });

        log.debug("Finished execution of " + this.jstacsTool.getToolName());
        return resultFiles.toArray(new String[] {});
    }

}
