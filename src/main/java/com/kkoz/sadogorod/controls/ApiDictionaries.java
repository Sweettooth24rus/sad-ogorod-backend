package com.kkoz.sadogorod.controls;

import com.kkoz.sadogorod.entities.dictionaries.EntityDictionary;
import com.kkoz.sadogorod.services.ServiceDictionaries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dictionaries")
public class ApiDictionaries {

    private final ServiceDictionaries serviceDictionaries;

    @GetMapping("/all")
    public Map<String, List<EntityDictionary>> getAllItems() {
        log.info("-> Getting all dictionaries");
        Map<String, List<EntityDictionary>> response = serviceDictionaries.getAllItems();
        log.info("<- Got all dictionaries");
        return response;
    }
}
