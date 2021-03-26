package de.jstacs.service.endpoints;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.jstacs.service.storage.StorageService;

@RestController
@RequestMapping("/files")
public class FilesEndpoint {

    private final StorageService storageService;
    private final Tika tika;

    @Autowired
    public FilesEndpoint(StorageService storageService) {
        this.storageService = storageService;
        this.tika = new Tika();
    }

    @PostMapping()
    public Map<String, String> setFileParameterContent(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, String> response = new HashMap<String, String>();
        String fileName = storageService.store(file);
        response.put("fileName", fileName);
        return response;
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {

        Resource fileResource = storageService.loadAsResource(filename);
        String mimeType = tika.detect(fileResource.getFile());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                .body(fileResource);
    }

    @GetMapping("/results/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveResultFile(@PathVariable String filename) throws IOException {

        Resource fileResource = storageService.loadAsResource("results/" + filename);
        String mimeType = tika.detect(fileResource.getFile());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                .body(fileResource);
    }

}
