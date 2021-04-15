package de.jstacs.service.storage;

public class StorageFileNotFoundException extends StorageException {

    private static final long serialVersionUID = -997343490138519294L;

    public StorageFileNotFoundException(String message) {
		super(message);
	}

	public StorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
    
}
