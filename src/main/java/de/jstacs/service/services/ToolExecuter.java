package de.jstacs.service.services;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import de.jstacs.service.tasks.ToolResultProcessTask;
import de.jstacs.service.tasks.ToolResultProcessTaskFactory;
import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.service.data.entities.ToolExecution.State;
import de.jstacs.service.data.repositories.ToolExecutionRepository;
import de.jstacs.service.tasks.ToolExecutionTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ToolExecuter {

    private final ToolResultProcessTaskFactory toolResultTaskFactory;

    private final ToolExecutionRepository toolExecutionRepository;

    public void execute(final ToolExecutionTask newTask) throws Exception {
        CompletableFuture.supplyAsync(() -> {
            try {
                return newTask.call();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                ToolExecution toolExecution = newTask.getToolExecution();
                toolExecution.setState(State.REJECTED);
                toolExecutionRepository.saveAndFlush(toolExecution);
                e.printStackTrace();
            }
            return null;
        }).thenApplyAsync((toolResult) -> {
            ToolResultProcessTask toolResultProcessTask = this.toolResultTaskFactory.create(toolResult, newTask.getToolExecution());
            String[] createdFiles = {};
            try {
                createdFiles = toolResultProcessTask.call();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                ToolExecution toolExecution = newTask.getToolExecution();
                toolExecution.setState(State.REJECTED);
                toolExecutionRepository.saveAndFlush(toolExecution);
                e.printStackTrace();
            }
            return createdFiles;
        }).thenAcceptAsync((createdFiles) -> {
            ToolExecution toolExecution = newTask.getToolExecution();
            toolExecution.setResults(createdFiles);
            toolExecution.setState(State.FULFILLED);
            toolExecutionRepository.saveAndFlush(toolExecution);
            log.debug("Created " + createdFiles.length + " result files for " + newTask.getToolName());
        });
    }

/*     public synchronized ResponseEntity<ToolTaskFileResult> getResult() {
        if (this.currentTask == null) {
            return new ResponseEntity<ToolTaskFileResult>(HttpStatus.NO_CONTENT);
        } else if (this.currentTask.isDone()) {
            List<String> fileResults = this.currentTask.getResult();
            ToolTaskFileResult result = new ToolTaskFileResult(fileResults);
            this.currentTask = null;
            return new ResponseEntity<ToolTaskFileResult>(result, HttpStatus.OK);
        } else {
            double progress = this.currentTask.getProgress();
            ToolTaskFileResult result = new ToolTaskFileResult(progress);
            return new ResponseEntity<ToolTaskFileResult>(result, HttpStatus.ACCEPTED);
        }
    } */

}
