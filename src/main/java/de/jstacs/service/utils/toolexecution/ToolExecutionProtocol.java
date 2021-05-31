package de.jstacs.service.utils.toolexecution;

import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.service.data.repositories.ToolExecutionRepository;
import de.jstacs.tools.Protocol;

public class ToolExecutionProtocol implements Protocol{

    private final ToolExecution toolExecution;

    private final ToolExecutionRepository toolExecutionRepository;

    private StringBuilder protocolBuilder;

    public ToolExecutionProtocol(final ToolExecution toolExecution, final ToolExecutionRepository toolExecutionRepository) {
        this.toolExecution = toolExecution;
        this.toolExecutionRepository = toolExecutionRepository;
        this.protocolBuilder = new StringBuilder();
    }

    @Override
    public void flush() {
        this.toolExecution.setProtocol(this.protocolBuilder.toString());
        this.toolExecutionRepository.save(this.toolExecution);
    }

    @Override
    public void append(String text) {
        this.protocolBuilder.append(System.lineSeparator());
        this.protocolBuilder.append(text);
        this.flush();
    }

    @Override
    public void appendHeading(String heading) {
        this.protocolBuilder.append(System.lineSeparator());
        this.protocolBuilder.append("## " + heading);
        this.flush();
    }

    @Override
    public void appendThrowable(Throwable error) {
        this.protocolBuilder.append(System.lineSeparator());
        this.protocolBuilder.append("> **ERROR:** " + error.getMessage());
        this.flush();
    }

    @Override
    public void appendVerbatim(String verbatim) {
        this.protocolBuilder.append(System.lineSeparator());
        this.protocolBuilder.append("```n" + verbatim + "\n```");
        this.flush();
    }

    @Override
    public void appendWarning(String warning) {
        this.protocolBuilder.append(System.lineSeparator());
        this.protocolBuilder.append("> **Warning:** " + warning);
        this.flush();
    }
    
}
