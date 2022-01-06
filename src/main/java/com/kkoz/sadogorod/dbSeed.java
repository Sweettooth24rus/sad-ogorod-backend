package com.kkoz.sadogorod;

import com.kkoz.sadogorod.entities.dictionaries.Role;
import com.kkoz.sadogorod.entities.dictionaries.TypeDocument;
import com.kkoz.sadogorod.repositories.RepoRole;
import com.kkoz.sadogorod.repositories.RepoTypeDocument;
import com.kkoz.sadogorod.repositories.RepoUzer;
import com.kkoz.sadogorod.utils.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@Profile("default")
@RequiredArgsConstructor
public class dbSeed implements CommandLineRunner {

    private final FileStorageUtil fileStorage;
    private final RepoRole repoRole;
    private final RepoTypeDocument repoTypeDocument;
    private final RepoUzer repoUzer;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.warn(" <-- Initialization started -->");

        createStorage();

        initRoles();
        initTypeDocuments();

        log.warn(" <-- Initialization ended -->");
    }

    private void createStorage() {
        fileStorage.createStorage();
    }

    private void initRoles() {
        List<Role> roleList = Arrays.asList(
                Role.ADMIN,
                Role.MEGA,
                Role.HYPER,
                Role.PLUS,
                Role.ORDINARY
        );
        repoRole.saveAll(roleList);
    }

    private void initTypeDocuments() {
        List<TypeDocument> typeDocumentList = Arrays.asList(
                TypeDocument.RECIPE_PHOTO
        );
        repoTypeDocument.saveAll(typeDocumentList);
    }
}
