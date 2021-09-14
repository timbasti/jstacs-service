package de.jstacs.service.endpoints;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.jstacs.service.storage.StorageProperties;
import de.jstacs.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5000",
        "https://jstacs-online.herokuapp.com" }, methods = { RequestMethod.OPTIONS, RequestMethod.GET,
                RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.HEAD }, exposedHeaders = {
                        "Accept-Ranges", "Content-Range", "Content-Encoding", "Content-Length" })
@Slf4j
@RestController
@RequestMapping("files")
@RequiredArgsConstructor
public class FilesEndpoint {

    private final StorageService storageService;

    private final StorageProperties storageProperties;

    @PostMapping
    public Map<String, String> saveFile(@RequestParam("file") MultipartFile file,
            @RequestParam("toolExecutionId") String toolExecutionId, @RequestHeader("user-id") String userId) {
        Map<String, String> response = new HashMap<String, String>();
        Path destinationDirectory = Paths.get(userId, toolExecutionId, this.storageProperties.getInputsLocation());
        Path filePath = storageService.store(file, destinationDirectory);
        log.debug("Stored file: " + filePath.toString());
        response.put("fileName", filePath.toString());
        return response;
    }

    @GetMapping
    public ResponseEntity<Resource> loadFile(@RequestParam String file) {
        Resource fileResource = storageService.loadAsResource(file);

        String mediaType = new String();
        try {
            Tika tika = new Tika();
            mediaType = tika.detect(fileResource.getFile());
        } catch (IOException e) {
            log.debug("Could not detect media type: " + fileResource.getFilename());
        }
        if (mediaType.isEmpty()) {
            mediaType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mediaType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileResource.getFilename() + "\"")
                .body(fileResource);
    }

}
