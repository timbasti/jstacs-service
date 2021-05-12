package de.jstacs.service.tasks;

public class TaskAlreadyRunningException extends TaskException {

    public TaskAlreadyRunningException() {
        super();
    }

    public TaskAlreadyRunningException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskAlreadyRunningException(String message) {
        super(message);
    }

    public TaskAlreadyRunningException(Throwable cause) {
        super(cause);
    }

}
