package de.jstacs.service.storage;

public class StorageFileNotFoundException extends StorageException {

    public StorageFileNotFoundException() {
        super();
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(Throwable cause) {
        super(cause);
    }
    
}
