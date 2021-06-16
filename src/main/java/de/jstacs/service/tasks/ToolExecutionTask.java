package de.jstacs.service.tasks;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import de.jstacs.results.savers.ResultSaver;
import de.jstacs.results.savers.ResultSaverLibrary;
import de.jstacs.results.savers.ResultSetResultSaver;
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

    private final Path resultDirectoryPath;

    private final StorageService storageService;

    public ToolExecutionTask(final JstacsTool jstacsTool, final ToolParameterSet toolParameterSet,
            final Protocol protocol, final ProgressUpdater progressUpdater, final Path resultDirectoryPath,
            final StorageService storageService, final ToolExecution toolExecution) {
        this.jstacsTool = jstacsTool;
        this.toolParameterSet = toolParameterSet;
        this.protocol = protocol;
        this.progressUpdater = progressUpdater;
        this.resultDirectoryPath = resultDirectoryPath;
        this.storageService = storageService;
        this.toolExecution = toolExecution;
        log.debug("Created ToolTask for: " + jstacsTool.getToolName());
    }

    public String getToolName() {
        return this.jstacsTool.getToolVersion();
    }

    @Override
    public String[] call() throws Exception {
        log.debug("Starting execution of " + this.jstacsTool.getToolName());
        ToolResult toolResult = this.jstacsTool.run(this.toolParameterSet, this.protocol, this.progressUpdater, 1);
        ResultSetResultSaver.register();
        ResultSaver<ToolResult> resultSaver = ResultSaverLibrary.getSaver(toolResult.getClass());
        Path resultsDirPath = Files.createDirectories(this.resultDirectoryPath);
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
