package com.kkoz.sadogorod;

import com.kkoz.sadogorod.entities.dictionaries.Role;
import com.kkoz.sadogorod.entities.dictionaries.TypeDocument;
import com.kkoz.sadogorod.entities.file.FileUpload;
import com.kkoz.sadogorod.entities.recipe.Difficulty;
import com.kkoz.sadogorod.entities.recipe.GroundType;
import com.kkoz.sadogorod.entities.recipe.LightType;
import com.kkoz.sadogorod.entities.recipe.Recipe;
import com.kkoz.sadogorod.entities.uzer.Uzer;
import com.kkoz.sadogorod.entities.uzer.UzerMail;
import com.kkoz.sadogorod.entities.uzer.UzerRole;
import com.kkoz.sadogorod.repositories.*;
import com.kkoz.sadogorod.utils.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@Profile("default")
@RequiredArgsConstructor
public class dbSeed implements CommandLineRunner {

    private final FileStorageUtil fileStorage;
    private final RepoRole repoRole;
    private final RepoLightType repoLightType;
    private final RepoGroundType repoGroundType;
    private final RepoDifficulty repoDifficulty;
    private final RepoTypeDocument repoTypeDocument;
    private final RepoFileUpload repoFileUpload;
    private final RepoUzer repoUzer;
    private final RepoUzerMail repoUzerMail;
    private final RepoRecipe repoRecipe;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.warn(" <-- Initialization started -->");

        createStorage();

        initRoles();
        initLightType();
        initGroungType();
        initDifficulty();
        initTypeDocuments();

        initUser();
        initRecipe();

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
                TypeDocument.RECIPE_PHOTO,
                TypeDocument.PICKLE_PHOTO,
                TypeDocument.TEA_PHOTO,
                TypeDocument.GARDEN_PHOTO,
                TypeDocument.WEED_PHOTO,
                TypeDocument.GREENHOUSE_PHOTO
        );
        repoTypeDocument.saveAll(typeDocumentList);
    }

    private void initUser() {

        Uzer uzer = new Uzer();
        uzer.setUsername("all_roles");
        uzer.setPassword("pwd"
        );
        uzer.setLastName("all_roles");
        uzer.setFirstName("all_roles");
        uzer.setPatronymicName("all_roles");
        uzer.setEmail("user_with_all_possible_roles@mail.ru");
        uzer.setRole(UzerRole.ADMIN);

        UzerMail userMail = new UzerMail();
        userMail.setUsername(uzer.getUsername());
        userMail.setActive(true);

        repoUzerMail.save(userMail);
        repoUzer.save(uzer);

    }

    private void initRecipe() {

        Recipe recipe = new Recipe();

        recipe.setName("Рецепт");
        recipe.setDescription("йцукенгшщзхзщшгеавыячсмитьорпавымитолшгнпморлботамлруаиомриуомриуомиулмулотмлуомлруиалмабвьлыдорпнрасптьбиотдолапрлорьипнеавкспмритошргпнавекнсапмриотлошргпнаевспмриотлощршгпнаермиотльдлщшргвотлауькдлцмвщшорваотльмавдлсщваошолимвс тдшозмвмашпдтомвдмтлваоцуомв дтвшоацдуто дмстшоавдтцо отвшмоыац одтмвшотдацуо модтвшоматцоу дмтш вмшат атмодвмшватаьт мошат октошзов тма ктотмшоа ваоутьа тзоу9 тшлпомарк03ауко8ашр оимрка43мк маи мукщагшкмту о вощмуа2шолмт авомкшалм овтакошмуол мтвмуакшлмутьм аоьшзуалштср мвиуцвтлцщлвыс всгимруцодльс шоргмшавнрацилувольуащзаишрщгрвлмц бьжлацуошприщгаотдлвцьдйщлушопаилом");

        recipe.setDays(50);
        recipe.setLightType(LightType.HIGH);
        recipe.setLightTime(54);
        recipe.setGroundType(GroundType.BLACK_SOIL);
        recipe.setMinTemperature(10);
        recipe.setMaxTemperature(100);
        recipe.setDifficulty(Difficulty.HARD);

        recipe.setComment("Комментарий");

        FileUpload file = new FileUpload();
        file.setCreated(LocalDateTime.now());
        file.setMimeType("application/pdf");
        UUID uuid = UUID.randomUUID();
        file.setOriginalFileName(uuid + ".jpg");
        file.setUuid(uuid);
        file.setSize(Integer.toUnsignedLong(1));
        file.setStorePath("./app/file-storage/Recipe/" + Integer.toUnsignedLong(1) + "/" + uuid + ".jpg");
        file.setTypeDocument(repoTypeDocument.getById(1));

        recipe.setFiles(List.of(repoFileUpload.save(file)));

        repoRecipe.save(recipe);
    }
}
