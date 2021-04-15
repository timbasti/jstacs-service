package de.jstacs.service.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.jstacs.results.savers.ResultSaver;
import de.jstacs.results.savers.ResultSaverLibrary;
import de.jstacs.results.savers.ResultSetResultSaver;
import de.jstacs.service.data.ToolTaskFileResult;
import de.jstacs.service.storage.FileSystemStorageService;
import de.jstacs.tools.JstacsTool;
import de.jstacs.tools.ProgressUpdater;
import de.jstacs.tools.Protocol;
import de.jstacs.tools.ToolParameterSet;
import de.jstacs.tools.ToolResult;
import de.jstacs.tools.ui.cli.CLI.SysProtocol;
import lombok.Getter;

public class JstacsToolTask {

    /* @Getter
    private final JstacsTool jstacsTool;

    @Getter
    private final ToolParameterSet toolParameterSet;

    @Getter
    private final Protocol protocol;

    @Getter
    private final ProgressUpdater progressUpdater;

    @Getter
    private final List<String> results = new ArrayList<String>();

    @Getter
    private boolean done;

    private final FileSystemStorageService storageService;

    public JstacsToolTask(final JstacsTool jstacsTool, final ToolParameterSet toolParameterSet,
            FileSystemStorageService storageService) {
        this.jstacsTool = jstacsTool;
        this.toolParameterSet = toolParameterSet;
        this.storageService = storageService;
        this.protocol = new SysProtocol();
        this.progressUpdater = new ProgressUpdater();
    }

    public void success(ToolResult toolResult) throws IOException {
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
            this.results.add(relativeResultPath);
        }

        this.done = true;
    }

    public ResponseEntity<ToolTaskFileResult> getResult() {
        HttpStatus status;
        if (this.done) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.ACCEPTED;
        }

        double progress = this.progressUpdater.getPercentage();
        ToolTaskFileResult jstacsToolTaskResponse = new ToolTaskFileResult(this.results, progress);

        ResponseEntity<ToolTaskFileResult> reponse = new ResponseEntity<ToolTaskFileResult>(
                jstacsToolTaskResponse, status);

        return reponse;
    } */

}
