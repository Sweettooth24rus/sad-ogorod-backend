package com.kkoz.sadogorod.controls;


import com.kkoz.sadogorod.dto.DtoModules;
import com.kkoz.sadogorod.entities.Modules;
import com.kkoz.sadogorod.services.ServiceModules;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/modules")
public class ApiModules {

    private final ServiceModules serviceModules;

    @GetMapping("/all")
    public List<DtoModules> getAll() {
        log.info("-> GET: Getting modules");
        List<DtoModules> modules = serviceModules.getAll();
        log.info("<- GET: Got modules");
        return modules;
    }

    @Transactional
    @PutMapping("/activity/{id}")
    public ResponseEntity<Map<String, String>> updateModuleActivity(@PathVariable @Min(1) Integer id) throws IOException {
        log.info("-> PUT: Updating module [{}] activity", id);
        Modules updatedModule = serviceModules.updateModuleActivity(id);
        Map<String, String> response = new HashMap<>();
        response.put("response", "Module [" + updatedModule.getId() + "] activity was updated to: " + updatedModule.getActivity());
        log.info("<- PUT: Module [{}] activity was updated to: {}", updatedModule.getId(), updatedModule.getActivity());
        return ResponseEntity.ok(response);
    }
}
