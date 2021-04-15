package de.jstacs.service.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import de.jstacs.results.savers.ResultSaver;
import de.jstacs.results.savers.ResultSaverLibrary;
import de.jstacs.results.savers.ResultSetResultSaver;
import de.jstacs.tools.ToolResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ToolResultProcessTask implements Callable<String[]> {

    private final ToolResult toolResult;

    private final Path targetDirectoryPath;

    @Override
    public String[] call() throws IOException {
        ResultSetResultSaver.register();
        ResultSaver<ToolResult> resultSaver = ResultSaverLibrary.getSaver(this.toolResult.getClass());
        Path resultsDirPath = Files.createDirectories(targetDirectoryPath);
        File resultsDir = resultsDirPath.toFile();

        File[] oldFiles = resultsDir.listFiles();
        for (File oldFile : oldFiles) {
            oldFile.delete();
        }

        resultSaver.writeOutput(toolResult, resultsDir);
        String[] results = resultsDir.list();

        return results;
    }
    
}
