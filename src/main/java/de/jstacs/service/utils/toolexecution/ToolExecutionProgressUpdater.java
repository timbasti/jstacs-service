package de.jstacs.service.utils.toolexecution;

import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.service.data.repositories.ToolExecutionRepository;
import de.jstacs.tools.ProgressUpdater;

public class ToolExecutionProgressUpdater extends ProgressUpdater {

    private ToolExecution toolExecution;

    private final ToolExecutionRepository toolExecutionRepository;

	public ToolExecutionProgressUpdater(final ToolExecution toolExecution, final ToolExecutionRepository toolExecutionRepository){
		this.toolExecution = toolExecution;
        this.toolExecutionRepository = toolExecutionRepository;
	}

    private void updateProgress() {
        double newProgress = this.getPercentage();
        this.toolExecution.setProgress(newProgress);
        this.toolExecutionRepository.save(this.toolExecution);
    }

    @Override
    public void setCurrent(double curr) {
        super.setCurrent(curr);
        this.updateProgress();
    }

    @Override
    public void add(double val) {
        super.add(val);
        this.updateProgress();
    }

    @Override
    public void setLast(double last) {
        super.setLast(last);
        this.updateProgress();
    }

    @Override
    public void setIndeterminate() {
        super.setIndeterminate();
        this.updateProgress();
    }

}
