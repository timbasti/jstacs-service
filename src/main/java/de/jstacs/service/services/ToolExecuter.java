package de.jstacs.service.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import de.jstacs.service.utils.toolexecution.ToolExecutionState;
import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.service.data.repositories.ToolExecutionRepository;
import de.jstacs.service.tasks.ToolExecutionTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ToolExecuter {

    private final ToolExecutionRepository toolExecutionRepository;

    @Async
    public void execute(String toolExecutionId, final ToolExecutionTask newTask) {
        ToolExecution toolExecution = this.toolExecutionRepository.findById(toolExecutionId).get();
        toolExecution.setState(ToolExecutionState.PENDING);
        // toolExecution = toolExecutionRepository.save(toolExecution);
        try {
            String[] resultFiles = newTask.call();
            toolExecution.setResults(resultFiles);
            toolExecution.setState(ToolExecutionState.FULFILLED);
            toolExecutionRepository.save(toolExecution);
            log.debug("Created " + resultFiles.length + " result files for " + newTask.getToolName());
        } catch (Exception e) {
            toolExecution.setState(ToolExecutionState.REJECTED);
            toolExecutionRepository.save(toolExecution);
        }




       /* CompletableFuture.supplyAsync(() -> {
            try {
                ToolExecution toolExecution = newTask.getToolExecution();
                toolExecution.setState(ToolExecutionState.PENDING);
                toolExecutionRepository.save(toolExecution);
                return newTask.call();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                ToolExecution toolExecution = newTask.getToolExecution();
                toolExecution.setState(ToolExecutionState.REJECTED);
                toolExecutionRepository.save(toolExecution);
                e.printStackTrace();
            }
            return null;
        }).thenApplyAsync((toolResult) -> {
            ToolResultProcessTask toolResultProcessTask = this.toolResultTaskFactory.create(toolResult, newTask.getToolExecution());
            try {
                return toolResultProcessTask.call();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                ToolExecution toolExecution = newTask.getToolExecution();
                toolExecution.setState(ToolExecutionState.REJECTED);
                toolExecutionRepository.save(toolExecution);
                e.printStackTrace();
            }
            return null;
        }).thenAcceptAsync((createdFiles) -> {
            ToolExecution toolExecution = newTask.getToolExecution();
            toolExecution.setResults(createdFiles);
            toolExecution.setState(ToolExecutionState.FULFILLED);
            toolExecutionRepository.save(toolExecution);
            log.debug("Created " + createdFiles.length + " result files for " + newTask.getToolName());
        }); */
    }

}
