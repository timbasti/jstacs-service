package de.jstacs.service.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

import de.jstacs.results.savers.ResultSaver;
import de.jstacs.results.savers.ResultSaverLibrary;
import de.jstacs.results.savers.ResultSetResultSaver;
import de.jstacs.service.storage.FileSystemStorageService;
import de.jstacs.tools.JstacsTool;
import de.jstacs.tools.ProgressUpdater;
import de.jstacs.tools.ToolParameterSet;
import de.jstacs.tools.ToolResult;

import de.jstacs.tools.ui.cli.CLI.SysProtocol;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ToolTask extends ForkJoinTask<ToolResult> {

    private static final long serialVersionUID = 5122487288990474815L;

    private final JstacsTool jstacsTool;

    private final ToolParameterSet toolParameterSet;

    private final SysProtocol protocol;

    private final ProgressUpdater progressUpdater;

    private final FileSystemStorageService storageService;

    @Getter
    private final List<String> result;
    
    private ToolResult rawResult;

    public ToolTask(final JstacsTool jstacsTool, final ToolParameterSet toolParameterSet, FileSystemStorageService storageService) {
        this.jstacsTool = jstacsTool;
        this.toolParameterSet = toolParameterSet;
        this.protocol = new SysProtocol();
        this.progressUpdater = new ProgressUpdater();
        this.storageService = storageService;
        this.result = new ArrayList<String>();
    }

    public String getToolName() {
        return this.jstacsTool.getToolName();
    }

    public double getProgress() {
        return this.progressUpdater.getPercentage();
    }

    @Override
    public ToolResult getRawResult() {
        return this.rawResult;
    }

    @Override
    protected void setRawResult(ToolResult rawResult) {
        this.rawResult = rawResult;
    }

    @Override
    protected boolean exec() {
        try {
            ToolResult rawResult = this.jstacsTool.run(this.toolParameterSet, this.protocol, this.progressUpdater, 1);
            this.setRawResult(rawResult);
            this.generateFileResults();
            return true;
        } catch (Exception e) {
            log.error("Execution failed for: " + this.jstacsTool.getToolName(), e);
            return false;
        }
    }

    private void generateFileResults() throws IOException {
        ToolResult toolResult = this.getRawResult();

        ResultSetResultSaver.register();
        ResultSaver<ToolResult> resultSaver = ResultSaverLibrary.getSaver(toolResult.getClass());

        File rootDir = this.storageService.getRootDir();
        Path resultsDirPath = Files.createDirectories(rootDir.toPath().resolve("results"));
        File resultsDir = resultsDirPath.toFile();

        File[] oldFiles = resultsDir.listFiles();
        for (File oldFile : oldFiles) {
            oldFile.delete();
        }

        resultSaver.writeOutput(toolResult, resultsDir);
        File[] results = resultsDir.listFiles();

        for (File file : results) {
            String relativeResultPath = storageService.relativizeFilePath(file.getAbsolutePath());
            this.result.add(relativeResultPath);
        }
    }

}
