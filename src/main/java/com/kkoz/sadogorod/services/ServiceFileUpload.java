package com.kkoz.sadogorod.services;

import com.kkoz.sadogorod.config.FileStorageConfig;
import com.kkoz.sadogorod.controls.exceptions.CannotDeleteFileException;
import com.kkoz.sadogorod.controls.exceptions.CannotShowFileException;
import com.kkoz.sadogorod.controls.exceptions.FileNotExistsException;
import com.kkoz.sadogorod.controls.exceptions.NotFoundException;
import com.kkoz.sadogorod.dto.file.DtoFileDelete;
import com.kkoz.sadogorod.dto.file.DtoFileUpload;
import com.kkoz.sadogorod.entities.dictionaries.EntityWithFiles;
import com.kkoz.sadogorod.entities.file.FileUpload;
import com.kkoz.sadogorod.entities.meta.MetaEntityWithFiles;
import com.kkoz.sadogorod.entities.recipe.Recipe;
import com.kkoz.sadogorod.repositories.RepoFileUpload;
import com.kkoz.sadogorod.repositories.RepoRecipe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceFileUpload {

    @Value("${file.extension.valid.extensions}")
    private String validExtensionsString;

    private final FileStorageConfig storageConfig;
    private final RepoFileUpload repoFileUpload;
    private final RepoRecipe repoRecipe;

    public boolean checkExtension(MultipartFile file) {
        if (Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".") == -1) {
            return false;
        }

        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        return this.validExtensionsString.contains(ext);
    }

    public DtoFileUpload upload(MultipartFile file) throws IOException {
        String ext = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        UUID uuid = UUID.randomUUID();

        String filename = uuid + ext;

        FileUpload fileUpload = new FileUpload();
        fileUpload.setOriginalFileName(file.getOriginalFilename());
        fileUpload.setMimeType(file.getContentType());
        fileUpload.setUuid(uuid);
        fileUpload.setSize(file.getSize());
        fileUpload.setCreated(LocalDateTime.now());
        fileUpload.setStorePath(this.storeTmp(filename, file.getBytes()));

        return new DtoFileUpload(repoFileUpload.save(fileUpload));
    }

    private String storeTmp(String filename, byte[] data) throws IOException {
        return this.store(storageConfig.getTmp(), filename, data);
    }

    public String store(String path, String filename, byte[] data) throws IOException {
        if (!path.endsWith(String.valueOf(File.separatorChar))) {
            path = path + File.separatorChar;
        }

        File fileToStore = new File(path + filename);
        Files.write(fileToStore.toPath(), data);
        return fileToStore.getPath();
    }

    public Map<String, Object> show(UUID uuid) {
        if (repoFileUpload.findByUuid(uuid).isPresent()) {
            FileUpload fileUpload = repoFileUpload.findByUuid(uuid).get();

            byte[] fileBytes;

            try {
                fileBytes = Files.readAllBytes(Paths.get(fileUpload.getStorePath()));
            } catch (NoSuchFileException e) {
                log.warn("_x File [{}] doesn't exist", fileUpload.getStorePath());
                throw new FileNotExistsException(fileUpload.getStorePath());
            } catch (IOException e) {
                log.warn("_x Can't show file [{}]", fileUpload.getStorePath());
                throw new CannotShowFileException(fileUpload.getStorePath());
            }

            String mediaType    = fileUpload.getMimeType().split("/")[0];
            String mediaSubType = fileUpload.getMimeType().split("/")[1];

            ResponseEntity<Resource> resource = ResponseEntity.ok()
                    .contentType(new MediaType(mediaType, mediaSubType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=" + URLEncoder.encode(fileUpload.getOriginalFileName(), StandardCharsets.UTF_8))
                    .body(new ByteArrayResource(fileBytes));

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("file", fileUpload.getStorePath());
            responseMap.put("resource", resource);

            return responseMap;
        }
        else {
            throw new NotFoundException("file", "UUID", uuid.toString());
        }
    }

    public String deleteFromRecipe(DtoFileDelete dtoFileDelete) {
        return this.delete(EntityWithFiles.RECIPE.getKey(), dtoFileDelete.getEntityId(), dtoFileDelete.getUuid());
    }

    private String delete(String entityName, Integer entityId, UUID uuid) {
        if (entityId != null) {
            if (repoFileUpload.findByUuid(uuid).isPresent()) {
                MetaEntityWithFiles entity = null;

                if (entityName.equals(EntityWithFiles.RECIPE.getKey())) {
                    entity = repoRecipe.getById(entityId);
                }

                assert entity != null;
                entity.getFiles().removeIf(
                        fileUpload -> fileUpload.getUuid().equals(uuid)
                );

                if (entityName.equals(EntityWithFiles.RECIPE.getKey())) {
                    repoRecipe.save((Recipe) entity);
                }

                FileUpload fileUpload = repoFileUpload.findByUuid(uuid).get();

                return this.deleteFromDatabaseAndServer(fileUpload);
            } else {
                throw new NotFoundException("file", "UUID", uuid.toString());
            }
        }
        else {
            if (repoFileUpload.findByUuid(uuid).isPresent()) {
                FileUpload fileUpload = repoFileUpload.findByUuid(uuid).get();
                return this.deleteFromDatabaseAndServer(fileUpload);
            }
            else {
                throw new NotFoundException("file", "UUID", uuid.toString());
            }
        }
    }

    private String deleteFromDatabaseAndServer(FileUpload fileUpload) {
        repoFileUpload.delete(fileUpload);
        try {
            Files.deleteIfExists(Paths.get(fileUpload.getStorePath()));
            return fileUpload.getStorePath();
        }
        catch (NoSuchFileException e) {
            log.warn("_x File [{}] doesn't exist", fileUpload.getStorePath());
            throw new FileNotExistsException(fileUpload.getStorePath());
        }
        catch (IOException e) {
            log.error("_x Can't delete file [{}]", fileUpload.getStorePath());
            throw new CannotDeleteFileException(fileUpload.getStorePath());
        }
    }

    public List<String> deleteFromServer(List<FileUpload> files) {
        List<String> deletedFilePaths = new ArrayList<>();
        for (FileUpload file : files) {
            try {
                Files.deleteIfExists(Path.of(file.getStorePath()));
                deletedFilePaths.add(file.getStorePath());
            }
            catch (IOException ignored){
            }
        }
        return deletedFilePaths;
    }

    public String getDir(Class<? extends MetaEntityWithFiles> entityClass, Integer id) {
        String dirPath = storageConfig.getPath() + entityClass.getSimpleName() + File.separatorChar + id;
        File dir = new File(dirPath);
        try {
            dir.mkdirs();
            return dir.getPath() + File.separatorChar;
        } catch (SecurityException e) {
            log.error("Unable to write file [{}]. Insufficient rights.", dirPath);
            return null;
        }
    }

    public void moveToStorage(List<FileUpload> files, String targetDir) {
        if (files == null || files.isEmpty()) {
            return;
        }

        for (FileUpload file : files) {
            if (file.getStorePath().contains(File.separatorChar + "file-tmp" + File.separatorChar)) {
                //  `log.debug("Trying to move file [{}] from file-tmp to file-storage...", file.getStorePath());

                String ext            = file.getOriginalFileName().substring(file.getOriginalFileName().lastIndexOf("."));
                String filename       = file.getUuid().toString();
                String replaceStorage = targetDir + filename + ext;

                try {
                    Files.move(Paths.get(file.getStorePath()), Paths.get(replaceStorage), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ignored /*e*/) {
                    //log.error("Can't move file [{}]: {}, cause: {}", file.getStorePath(), e.getMessage(), e.getCause());
                }

                file.setStorePath(replaceStorage);
            }
        }
        repoFileUpload.saveAll(files);
    }

}
