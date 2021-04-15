package de.jstacs.service.tasks;

import java.util.concurrent.Callable;

import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.tools.JstacsTool;
import de.jstacs.tools.ProgressUpdater;
import de.jstacs.tools.Protocol;
import de.jstacs.tools.ToolParameterSet;
import de.jstacs.tools.ToolResult;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ToolExecutionTask implements Callable<ToolResult> {

    @Getter
    private final ToolExecution toolExecution;

    private final JstacsTool jstacsTool;

    @Getter
    private final ToolParameterSet toolParameterSet;

    private final Protocol protocol;

    private final ProgressUpdater progressUpdater;

    public ToolExecutionTask(final ToolExecution toolExecution, final JstacsTool jstacsTool, final ToolParameterSet toolParameterSet, final Protocol protocol, final ProgressUpdater progressUpdater) {
        this.toolExecution = toolExecution;
        this.jstacsTool = jstacsTool;
        this.toolParameterSet = toolParameterSet;
        this.protocol = protocol;
        this.progressUpdater = progressUpdater;
        log.debug("Created ToolTask for: " + jstacsTool.getToolName());
    }

    public String getToolName() {
        return this.jstacsTool.getToolVersion();
    }

    @Override
    public ToolResult call() throws Exception {
        log.debug("Starting execution of " + this.jstacsTool.getToolName());
        ToolResult toolResult = this.jstacsTool.run(this.toolParameterSet, this.protocol, this.progressUpdater, 1);
        log.debug("Finished execution of " + this.jstacsTool.getToolName());
        return toolResult;
    }

}
