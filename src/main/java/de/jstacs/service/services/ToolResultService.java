package de.jstacs.service.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import de.jstacs.service.tasks.ToolResultProcessTask;
import de.jstacs.tools.ToolResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ToolResultService {
    
    public CompletableFuture<Integer> execute(final ToolResult result) {
        CompletableFuture<Integer> future = new CompletableFuture<Integer>();
        /* future.completeAsync(() -> {
            return toolResultTask.call();
        });
        future.whenComplete((result, exception) -> {
            log.debug("Created " + result + " result files");
        }); */

        return future;
    }

}
