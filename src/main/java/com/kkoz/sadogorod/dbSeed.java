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
        initRecipe1();
        initRecipe2();

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

        recipe.setName("Огурец");
        recipe.setDescription("Огурцы выращивают рассадным и безрассадным методом. " +
                "Посев семян огурца на рассаду производят примерно за месяц до высадки на участок. " +
                "Семена огурца предварительно следует замочить и прорастить - это ускорит появление всходов. " +
                "Непосредственно в открытый грунт рассаду огурцов высаживают, когда почва достаточно прогреется " +
                "(примерно в конце мая в средней полосе России, в начале мая - в центральной Европе). " +
                "При безрассадном методе посев набухших или проросших семян огурца производят в почву, " +
                "температура которой в верхнем слое не ниже 13-15 градусов, иначе семена просто разлагаются. " +
                "Глубина заделки семян огурца – около 2 см, плотность – 5-7 растений на квадратный метр. " +
                "Сажать огурцы лучше по нескольку разных сортов рядом, это улучшает опыляемость растений и " +
                "способствует более высоким урожаям.\nПочва для огурцов должна быть в меру рыхлая, " +
                "хорошо удерживающая влагу и очень плодородная. Поскольку корневая система огурца небольшая, " +
                "то очень удобно выращивать огурцы, внося органику локально – в посадочные ямы или траншеи. " +
                "Выкапывают лунку или траншею на глубину около 40 см, укладывают слой органики, перемешивают с почвой, " +
                "сверху засыпают уже чистой почвой, в которую высаживают огурцы. Разлагаясь, органика выделяет много тепла, " +
                "что ускоряет рост и развитие растений, а в последующем служит отличной подкормкой практически на весь сезон.\n" +
                "Место для выращивания огурцов должно хорошо освещаться солнцем (допустима полутень) и " +
                "быть защищенным от ветра. В частности, при устройстве гряд в открытом грунте в качестве " +
                "'живого заслона' можно использовать кукурузу, посеяв её в две строчки по сторонам огуречной грядки, " +
                "оставив открытой южную сторону.\n" +
                "Оптимальная температура для нормального развития огурцов (как и других тыквенных культур) " +
                "колеблется в пределах 23-30 градусов. Температура воздуха ниже 15 градусов приводит к угнетению и " +
                "остановке роста растений на любой стадии развития. Заморозки для огурцов губительны " +
                "(особенно для молодых неокрепших растений), перепады температуры тормозят рост. " +
                "Поэтому в Северной Европе и средней полосе России часто огурцы выращивают под пленочными укрытиями, " +
                "хотя бы в первую половину лета.");
        recipe.setDays(50);
        recipe.setLightType(LightType.LOW);
        recipe.setLightTime(14);
        recipe.setGroundType(GroundType.BLACK_SOIL);
        recipe.setMinTemperature(15);
        recipe.setMaxTemperature(30);
        recipe.setDifficulty(Difficulty.HARD);

        recipe.setComment("Какой-то комментарий к огурцам");

        FileUpload file = new FileUpload();
        file.setCreated(LocalDateTime.now());
        file.setMimeType("image/jpg");
        UUID uuid = UUID.fromString("c83317be-647e-4a7a-9064-cf740711678d");//UUID.randomUUID();
        file.setOriginalFileName(uuid + ".jpg");
        file.setUuid(uuid);
        file.setSize(367L);
        file.setStorePath("./app/file-storage/Recipe/" + Integer.toUnsignedLong(1) + "/" + uuid + ".jpg");
        file.setTypeDocument(repoTypeDocument.getById(1));

        recipe.setFiles(List.of(repoFileUpload.save(file)));

        repoRecipe.save(recipe);
    }

    private void initRecipe1() {

        Recipe recipe = new Recipe();

        recipe.setName("Помидор");
        recipe.setDescription("Очень длинное описание выращивания помидоров");
        recipe.setDays(84);
        recipe.setLightType(LightType.MEDIUM);
        recipe.setLightTime(12);
        recipe.setGroundType(GroundType.SANDY_LOAM);
        recipe.setMinTemperature(10);
        recipe.setMaxTemperature(25);
        recipe.setDifficulty(Difficulty.NORMAL);

        recipe.setComment("Какой-то комментарий к помидорам");

        FileUpload file = new FileUpload();
        file.setCreated(LocalDateTime.now());
        file.setMimeType("image/jpg");
        UUID uuid = UUID.fromString("c83317be-647e-4a7a-9064-cf740711678e");//UUID.randomUUID();
        file.setOriginalFileName(uuid + ".jpg");
        file.setUuid(uuid);
        file.setSize(84L);
        file.setStorePath("./app/file-storage/Recipe/" + Integer.toUnsignedLong(2) + "/" + uuid + ".jpg");
        file.setTypeDocument(repoTypeDocument.getById(1));

        recipe.setFiles(List.of(repoFileUpload.save(file)));

        repoRecipe.save(recipe);
    }

    private void initRecipe2() {

        Recipe recipe = new Recipe();

        recipe.setName("Морковка");
        recipe.setDescription("Очень длинное описание выращивания капусты");
        recipe.setDays(77);
        recipe.setLightType(LightType.LOW);
        recipe.setLightTime(5);
        recipe.setGroundType(GroundType.PEAT_BOG);
        recipe.setMinTemperature(2);
        recipe.setMaxTemperature(24);
        recipe.setDifficulty(Difficulty.VERY_EASY);

        recipe.setComment("Какой-то комментарий к капусте");

        FileUpload file = new FileUpload();
        file.setCreated(LocalDateTime.now());
        file.setMimeType("image/jpg");
        UUID uuid = UUID.fromString("c83317be-647e-4a7a-9064-cf740711678f");//UUID.randomUUID();
        file.setOriginalFileName(uuid + ".jpg");
        file.setUuid(uuid);
        file.setSize(72L);
        file.setStorePath("./app/file-storage/Recipe/" + Integer.toUnsignedLong(3) + "/" + uuid + ".jpg");
        file.setTypeDocument(repoTypeDocument.getById(1));

        recipe.setFiles(List.of(repoFileUpload.save(file)));

        repoRecipe.save(recipe);
    }
}
