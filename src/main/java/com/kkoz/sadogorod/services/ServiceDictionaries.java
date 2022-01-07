package com.kkoz.sadogorod.services;

import com.kkoz.sadogorod.entities.dictionaries.EntityDictionary;
import com.kkoz.sadogorod.repositories.RepoRole;
import com.kkoz.sadogorod.repositories.RepoTypeDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceDictionaries {

    private final RepoRole repoRole;
    private final RepoTypeDocument repoTypeDocument;

    public Map<String, List<EntityDictionary>> getAllItems() {

        log.info(">> getting all dictionaries");
        Map<String, List<EntityDictionary>> dictionaries = new HashMap<>();

        log.debug(" _. getting dictionary Role");
        List<EntityDictionary> dicRole = new ArrayList<>(repoRole.findAll());
        log.trace("  x. dictionary Role:\t{}", dicRole);
        dictionaries.put("UserRole", dicRole);

        log.debug(" _. getting dictionary TypeDocument");
        List<EntityDictionary> dicTypeDocument = new ArrayList<>(repoTypeDocument.findAll());
        log.trace("  x. dictionary TypeDocument:\t{}", dicTypeDocument);
        dictionaries.put("TypeDocument", dicTypeDocument);

        return dictionaries;
    }
}
