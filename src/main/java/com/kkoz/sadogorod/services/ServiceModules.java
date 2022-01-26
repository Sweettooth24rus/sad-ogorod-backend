package com.kkoz.sadogorod.services;

import com.kkoz.sadogorod.dto.DtoModules;
import com.kkoz.sadogorod.entities.Modules;
import com.kkoz.sadogorod.repositories.RepoModules;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceModules {

    @AllArgsConstructor
    class MyFilenameFilter implements FilenameFilter {

        String initials;

        @Override
        public boolean accept(File dir, String name) {
            return name.startsWith(initials);
        }
    }

    private final RepoModules repoModules;

    public List<DtoModules> getAll() {

        this.findModules();

        return repoModules.findAll().stream()
                .map(DtoModules::new)
                .collect(Collectors.toList());
    }

    public Modules updateModuleActivity(Integer id) {
        Modules modules = repoModules.getById(id);

        modules.setActivity(!modules.getActivity());

        return repoModules.save(modules);
    }

    private void findModules() {
        List<Modules> oldModules = repoModules.findAll();
        List<Modules> modules = new ArrayList<>();
        List<Modules> newModules = new ArrayList<>();
        File directory = new File("E:\\sad-ogorod-backend\\src\\main\\java\\com\\kkoz\\sadogorod\\modules");

        for (final File fileEntry : Objects.requireNonNull(directory.listFiles())) {
            newModules.add(new Modules(fileEntry.getName()));
        }

        for (Modules module:
             newModules) {
            Modules tmp = oldModules.stream().filter(mdl -> mdl.getName().equals(module.getName())).findFirst().orElse(null);
            if (tmp != null) {
                module.setActivity(tmp.getActivity());
            }
            modules.add(module);
        }

        repoModules.deleteAll();
        repoModules.saveAll(modules);
    }
}
