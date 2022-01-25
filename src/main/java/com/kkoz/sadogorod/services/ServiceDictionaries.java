package com.kkoz.sadogorod.services;

import com.kkoz.sadogorod.entities.dictionaries.EntityDictionary;
import com.kkoz.sadogorod.repositories.*;
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
    private final RepoLightType repoLightType;
    private final RepoGroundType repoGroundType;
    private final RepoDifficulty repoDifficulty;

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

        log.debug(" _. getting dictionary LightType");
        List<EntityDictionary> dicLightType = new ArrayList<>(repoLightType.findAll());
        log.trace("  x. dictionary LightType:\t{}", dicLightType);
        dictionaries.put("LightType", dicLightType);

        log.debug(" _. getting dictionary GroundType");
        List<EntityDictionary> dicGroundType = new ArrayList<>(repoGroundType.findAll());
        log.trace("  x. dictionary GroundType:\t{}", dicGroundType);
        dictionaries.put("GroundType", dicGroundType);

        log.debug(" _. getting dictionary Difficulty");
        List<EntityDictionary> dicDifficulty = new ArrayList<>(repoDifficulty.findAll());
        log.trace("  x. dictionary Difficulty:\t{}", dicDifficulty);
        dictionaries.put("Difficulty", dicDifficulty);

        return dictionaries;
    }
}
