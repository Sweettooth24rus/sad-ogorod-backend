package com.kkoz.sadogorod.controls;

import com.kkoz.sadogorod.controls.exceptions.FileExtensionException;
import com.kkoz.sadogorod.controls.exceptions.FileStoreException;
import com.kkoz.sadogorod.dto.file.DtoFileUpload;
import com.kkoz.sadogorod.services.ServiceFileUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class ApiFileUpload {

    private final ServiceFileUpload serviceFileUpload;

    @PostMapping("/upload")
    public ResponseEntity<DtoFileUpload> upload(@RequestParam("document") MultipartFile file) {

        log.info("-> POST: Uploading file [name: {}, size: {}] to file-tmp", file.getOriginalFilename(), file.getSize());

        if (!serviceFileUpload.checkExtension(file)) {
            log.info("<- POST: File [{}] has an invalid extension", file.getOriginalFilename());
            throw new FileExtensionException(file.getOriginalFilename());
        }

        DtoFileUpload response;
        try {
            response = serviceFileUpload.upload(file);
        } catch (IOException e) {
            log.info("<- POST: Can't write file [{}]: {}", file.getOriginalFilename(), e.getMessage());
            throw new FileStoreException();
        }

        log.info("<- POST: File [{}] uploaded to [{}]", file.getOriginalFilename(), response.getStorePath());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Resource> show(@PathVariable UUID uuid) {
        log.info("-> GET: Showing file with uuid [{}]", uuid);
        Map<String, Object> response = serviceFileUpload.show(uuid);
        log.info("<- GET: Resource for file [{}]", response.get("file"));
        return (ResponseEntity<Resource>) response.get("resource");
    }
}
