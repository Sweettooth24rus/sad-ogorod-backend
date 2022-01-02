package com.kkoz.sadogorod;

import com.kkoz.sadogorod.entities.dictionaries.Role;
import com.kkoz.sadogorod.repositories.RepoRole;
import com.kkoz.sadogorod.repositories.RepoUzer;
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

    RepoRole repoRole;
    RepoUzer repoUzer;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.warn(" <-- Initialization started (lol) -->");

        initRoles();

        log.warn(" <-- Initialization ended -->");
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
}
