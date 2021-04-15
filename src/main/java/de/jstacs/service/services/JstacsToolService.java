package de.jstacs.service.services;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.jstacs.service.data.ToolTaskFileResult;
import de.jstacs.service.tasks.JstacsToolTask;
import de.jstacs.tools.JstacsTool;
import de.jstacs.tools.ProgressUpdater;
import de.jstacs.tools.Protocol;
import de.jstacs.tools.ToolParameterSet;
import de.jstacs.tools.ToolResult;

@Service
public class JstacsToolService {

    /* private final AtomicReference<JstacsToolTask> taskReference;

    private final AtomicReference<CompletableFuture<ToolResult>> futureReference;

    public JstacsToolService () {
        this.taskReference = new AtomicReference<JstacsToolTask>();
        this.futureReference = new AtomicReference<CompletableFuture<ToolResult>>();
    }

    public void execute(final JstacsToolTask newTask) throws Exception {
        JstacsToolTask currentTask = taskReference.get();
        CompletableFuture<ToolResult> currentFuture = futureReference.get();
        if (currentTask != null && !currentTask.isDone()) {
            currentFuture.cancel(true);
        }

        CompletableFuture<ToolResult> newFuture = new CompletableFuture<ToolResult>();
        taskReference.set(newTask);
        futureReference.set(newFuture);

        newFuture.completeAsync(() -> {
            ToolResult toolResult = null;
            try {
                JstacsTool jstacsTool = newTask.getJstacsTool();
                ToolParameterSet toolParameterSet = newTask.getToolParameterSet();
                Protocol protocol = newTask.getProtocol();
                ProgressUpdater progressUpdater = newTask.getProgressUpdater();
                toolResult = jstacsTool.run(toolParameterSet, protocol, progressUpdater, 1);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return toolResult;
        });
        newFuture.whenCompleteAsync((toolResult, throwable) -> {
            try {
                newTask.success(toolResult);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public synchronized ResponseEntity<ToolTaskFileResult> getResult() {
        JstacsToolTask currentTask = taskReference.get();
        return currentTask.getResult();
    } */

}
