package de.jstacs.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	Path store(MultipartFile file);

    Path store(MultipartFile file, Path path);

	Stream<Path> loadAll();

	Path load(String fileName);

	Resource loadAsResource(String filename);

    Path locate(String fileName);

	void deleteAll();

}
