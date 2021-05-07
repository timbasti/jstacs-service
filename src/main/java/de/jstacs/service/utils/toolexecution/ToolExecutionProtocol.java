package de.jstacs.service.utils.toolexecution;

import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.service.data.repositories.ToolExecutionRepository;
import de.jstacs.tools.Protocol;

public class ToolExecutionProtocol implements Protocol{

    private final ToolExecution toolExecution;

    private final ToolExecutionRepository toolExecutionRepository;

    public ToolExecutionProtocol(final ToolExecution toolExecution, final ToolExecutionRepository toolExecutionRepository) {
        this.toolExecution = toolExecution;
        this.toolExecutionRepository = toolExecutionRepository;
        
        String executionName = this.toolExecution.getName();
        String protocol = executionName.isEmpty() ? "# Execution for " + this.toolExecution.getTool().getName() : executionName;
        this.toolExecution.setProtocol(protocol);
        this.flush();
    }

    @Override
    public void flush() {
        this.toolExecutionRepository.save(this.toolExecution);
    }

    @Override
    public void append(String text) {
        String currentProtocol = this.toolExecution.getProtocol();
        String nextProtocol = currentProtocol + "\n" + text;
        this.toolExecution.setProtocol(nextProtocol);
        this.flush();
    }

    @Override
    public void appendHeading(String heading) {
        String newProtocolEntry = "## " + heading;
        String currentProtocol = this.toolExecution.getProtocol();
        String nextProtocol = currentProtocol + "\n" + newProtocolEntry;
        this.toolExecution.setProtocol(nextProtocol);
        this.flush();
    }

    @Override
    public void appendThrowable(Throwable error) {
        String newProtocolEntry = "> **ERROR:** " + error.getMessage();
        String currentProtocol = this.toolExecution.getProtocol();
        String nextProtocol = currentProtocol + "\n" + newProtocolEntry;
        this.toolExecution.setProtocol(nextProtocol);
        this.flush();
    }

    @Override
    public void appendVerbatim(String verbatim) {
        String newProtocolEntry = "```n" + verbatim + "\n```";
        String currentProtocol = this.toolExecution.getProtocol();
        String nextProtocol = currentProtocol + "\n" + newProtocolEntry;
        this.toolExecution.setProtocol(nextProtocol);
        this.flush();
    }

    @Override
    public void appendWarning(String warning) {
        String newProtocolEntry = "> **Warning:** " + warning;
        String currentProtocol = this.toolExecution.getProtocol();
        String nextProtocol = currentProtocol + "\n" + newProtocolEntry;
        this.toolExecution.setProtocol(nextProtocol);
        this.flush();
    }
    
}
