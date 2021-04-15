package de.jstacs.service.storage;

public class StorageException extends RuntimeException {

    private static final long serialVersionUID = 4348591169084470082L;

    public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

}
