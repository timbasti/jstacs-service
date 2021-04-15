package de.jstacs.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        Path rootPath = Paths.get(properties.getRootLocation());
        this.rootLocation = rootPath.isAbsolute() ? rootPath : rootPath.toAbsolutePath();
    }

    @Override
    public Path store(MultipartFile file) {
        return this.store(file, Paths.get("temp"));
    }

    @Override
    public Path store(MultipartFile file, Path path) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path targetLocation = this.rootLocation.resolve(path).normalize();
            Path filePath = Paths.get(file.getOriginalFilename());
            Path destinationFilePath = targetLocation.resolve(filePath).normalize();
            if (!destinationFilePath.startsWith(this.rootLocation)) {
                // This is a security check
                throw new StorageException("Cannot store file outside storage directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.createDirectories(targetLocation);
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
                return this.rootLocation.relativize(destinationFilePath);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String fileName) {
        Path filePath = Paths.get(fileName);
        if (filePath.isAbsolute()) {
            if (!filePath.startsWith(this.rootLocation)) {
                // This is a security check
                throw new StorageException("Cannot load file outside storage directory.");
            }
        } else {
            filePath = rootLocation.resolve(filePath);
        }
        return filePath;
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path filePath = load(filename);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public Path locate(String fileName) {
        Path loadedPath = this.load(fileName);
        return rootLocation.relativize(loadedPath);
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

}
