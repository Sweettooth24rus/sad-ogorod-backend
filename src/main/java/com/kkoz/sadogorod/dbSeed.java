package com.kkoz.sadogorod;

import com.kkoz.sadogorod.entities.dictionaries.Role;
import com.kkoz.sadogorod.entities.dictionaries.TypeDocument;
import com.kkoz.sadogorod.entities.recipe.Difficulty;
import com.kkoz.sadogorod.entities.recipe.GroundType;
import com.kkoz.sadogorod.entities.recipe.LightType;
import com.kkoz.sadogorod.entities.uzer.Uzer;
import com.kkoz.sadogorod.entities.uzer.UzerRole;
import com.kkoz.sadogorod.repositories.*;
import com.kkoz.sadogorod.security.jwt.secret_key.ServiceJwtSecretKey;
import com.kkoz.sadogorod.utils.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final ServiceJwtSecretKey serviceJwtSecretKey;
    private final RepoRole repoRole;
    private final RepoLightType repoLightType;
    private final RepoGroundType repoGroundType;
    private final RepoDifficulty repoDifficulty;
    private final RepoTypeDocument repoTypeDocument;
    private final RepoUzer repoUzer;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.warn(" <-- Initialization started -->");

        initSecretKey();
        createStorage();

        initRoles();
        initLightType();
        initGroungType();
        initDifficulty();
        initTypeDocuments();

        initUser();

        log.warn(" <-- Initialization ended -->");
    }

    private void initSecretKey() {
        serviceJwtSecretKey.generateNewActive();
        serviceJwtSecretKey.generateNewActive();
        serviceJwtSecretKey.generateNewActive();

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

    private void initLightType() {
        List<LightType> lightTypeList = Arrays.asList(
                LightType.DARK,
                LightType.LOW,
                LightType.MEDIUM,
                LightType.HIGH,
                LightType.VERY_HIGH
        );
        repoLightType.saveAll(lightTypeList);
    }

    private void initGroungType() {
        List<GroundType> groundTypeList = Arrays.asList(
                GroundType.ALUMINA,
                GroundType.LOAM,
                GroundType.SANDSTONE,
                GroundType.SANDY_LOAM,
                GroundType.PEAT_BOG,
                GroundType.PODZOLIC,
                GroundType.SOD_PODZOLIC,
                GroundType.BLACK_SOIL
        );
        repoGroundType.saveAll(groundTypeList);
    }

    private void initDifficulty() {
        List<Difficulty> difficultyList = Arrays.asList(
                Difficulty.VERY_EASY,
                Difficulty.EASY,
                Difficulty.NORMAL,
                Difficulty.HARD,
                Difficulty.VERY_HARD
        );
        repoDifficulty.saveAll(difficultyList);
    }

    private void initTypeDocuments() {
        List<TypeDocument> typeDocumentList = Arrays.asList(
                TypeDocument.RECIPE_PHOTO
        );
        repoTypeDocument.saveAll(typeDocumentList);
    }

    private void initUser() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Uzer uzer = new Uzer();
        uzer.setUsername("all_roles");
        uzer.setPassword(
                passwordEncoder.encode("pwd")
        );
        uzer.setLastName("all_roles");
        uzer.setFirstName("all_roles");
        uzer.setPatronymicName("all_roles");
        uzer.setEmail("user_with_all_possible_roles@mail.ru");
        uzer.setRole(UzerRole.ADMIN);

        repoUzer.save(uzer);

    }
}
