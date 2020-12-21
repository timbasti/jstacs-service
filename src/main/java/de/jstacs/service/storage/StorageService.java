package de.jstacs.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init() throws IOException;

    String store(MultipartFile file) throws IOException;
    
    String create(String fileName, String content) throws IOException;

	Stream<Path> loadAll() throws IOException;

	Path load(String filename);

	Resource loadAsResource(String filename) throws MalformedURLException;

	void deleteAll() throws IOException;

}
