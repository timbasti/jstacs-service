package de.jstacs.service.services;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.jstacs.service.data.ToolTaskFileResult;
import de.jstacs.service.tasks.ToolTask;

@Service
public class ToolService {

    private ToolTask currentTask;

    private ForkJoinPool pool;

    public ToolService () {
        this.pool = ForkJoinPool.commonPool();
    }

    public void execute(final ToolTask newTask) throws Exception {
        if (this.currentTask != null && !this.currentTask.isDone()) {
            this.currentTask.cancel(true);
        }

        this.pool.submit(newTask);
        this.currentTask = newTask;
    }

    public synchronized ResponseEntity<ToolTaskFileResult> getResult() {
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
    }

}
