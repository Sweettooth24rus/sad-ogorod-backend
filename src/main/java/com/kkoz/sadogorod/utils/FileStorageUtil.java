package com.kkoz.sadogorod.utils;

import com.kkoz.sadogorod.config.FileStorageConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStorageUtil {

    private final FileStorageConfig storageConfig;

    public void createStorage() {
        File storage = new File(storageConfig.getPath());
        File tmpStorage = new File(storageConfig.getTmp());

        if (storage.mkdirs()) {
            log.debug("[FileStorage] Storage created: [{}]", storage.getPath());
        }
        else {
            log.debug("[FileStorage] Storage [{}] already exists.", storage.getPath());
        }

        if (tmpStorage.mkdirs()) {
            log.debug("[FileStorage] Temp storage created: [{}]", tmpStorage.getPath());
        }
        else {
            log.debug("[FileStorage] Temp storage [{}] already exists.", tmpStorage.getPath());
        }
    }

}
