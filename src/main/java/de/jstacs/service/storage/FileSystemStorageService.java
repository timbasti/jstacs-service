package de.jstacs.service.storage;

import java.io.File;
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
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String store(MultipartFile file) throws IOException {
        Path filePath = Paths.get(file.getOriginalFilename());
        Path absoluteFilePath = this.resolveFilePath(filePath);
        InputStream inputStream = file.getInputStream();
        Files.copy(inputStream, absoluteFilePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString();
    }

    @Override
    public String create(String fileName, String content) throws IOException {
        Path filePath = Paths.get(fileName);
        Path absoluteFilePath = this.resolveFilePath(filePath);
        Files.writeString(absoluteFilePath, content);
        return filePath.toString();
    }

    @Override
    public Stream<Path> loadAll() throws IOException {
        return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize);

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) throws MalformedURLException {
        Path file = load(filename);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        }
        return null;
    }

    @Override
    public void deleteAll() throws IOException {
        FileSystemUtils.deleteRecursively(rootLocation);
    }

    @Override
    public void init() throws IOException {
        Files.createDirectories(rootLocation);
    }

    public String resolveFilePath(String fileName) {
        Path filePath = Paths.get(fileName);
        Path absoluteFilePath = this.resolveFilePath(filePath);
        return absoluteFilePath.toString();
    }

    public Path resolveFilePath(Path filePath) {
        Path relativeFilePath = this.rootLocation.resolve(filePath).normalize();
        Path absoluteFilePath = relativeFilePath.toAbsolutePath();
        return absoluteFilePath;
    }

    public String relativizeFilePath(String fileName) {
        Path filePath = Paths.get(fileName);
        Path relativeFilePath = this.relativizeFilePath(filePath);
        return relativeFilePath.toString();
    }

    public Path relativizeFilePath(Path filePath) {
        Path absoluteRootPath = this.rootLocation.toAbsolutePath();
        return absoluteRootPath.relativize(filePath);
    }

    public File getRootDir() {
        return this.rootLocation.toFile();
    }

}
