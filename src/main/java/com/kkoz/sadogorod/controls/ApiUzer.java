package com.kkoz.sadogorod.controls;


import com.kkoz.sadogorod.dto.uzer.DtoUzer;
import com.kkoz.sadogorod.dto.uzer.DtoUzerPagination;
import com.kkoz.sadogorod.dto.uzer.DtoUzerUpdate;
import com.kkoz.sadogorod.entities.uzer.Uzer;
import com.kkoz.sadogorod.entities.uzer.UzerRole;
import com.kkoz.sadogorod.security.meta_annotation.HasRoleAdmin;
import com.kkoz.sadogorod.security.meta_annotation.HasRoleAny;
import com.kkoz.sadogorod.services.ServiceUzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/user")
public class ApiUzer {

    private final ServiceUzer serviceUzer;

    public ApiUzer(ServiceUzer serviceUzer) {
        this.serviceUzer = serviceUzer;
    }

    @HasRoleAdmin
    @GetMapping("/all")
    public Page<DtoUzerPagination> getPage(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                           @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                           @RequestParam(required = false, defaultValue = "-id") String sort,
                                           @RequestParam(required = false) String username,
                                           @RequestParam(required = false) String lastName,
                                           @RequestParam(required = false) String firstName,
                                           @RequestParam(required = false) String patronymicName,
                                           @RequestParam(required = false) String role,
                                           @RequestParam(required = false) String isActive) {
        log.info("-> GET: Getting user page (page: {}, size: {}, sort: {}, username: {}, lastName: {}, firstName: {}, patronymicName: {}," +
                "role: {}, isActive: {})", page, size, sort, username, lastName, firstName, patronymicName, role, isActive);
        Page<DtoUzerPagination> uzerPage = serviceUzer.getPage(page, size, sort, username, lastName, firstName, patronymicName, role, isActive);
        log.info("<- GET: Got user page (page: {}, size: {}, sort: {}, username: {}, lastName: {}, firstName: {}, patronymicName: {}," +
                "role: {}, isActive: {})", page, size, sort, username, lastName, firstName, patronymicName, role, isActive);
        return uzerPage;
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createUzer(@Valid @RequestBody DtoUzer uzer) {
        log.info("-> POST: Adding new user: {}", uzer);
        Uzer createdUzer = serviceUzer.createUzer(uzer);
        Map<String, String> response = new HashMap<>();
        response.put("id", createdUzer.getId().toString());
        response.put("response", "User created with id [" + createdUzer.getId() + "]");
        log.info("<- POST: User created with id [{}]", createdUzer.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @HasRoleAdmin
    @GetMapping("/{id}")
    public DtoUzer getUzer(@PathVariable @Min(1) Integer id) {
        log.info("-> GET: Getting Uzer with id [{}]", id);
        DtoUzer dtoUzer = new DtoUzer(serviceUzer.getById(id));
        log.info("<- GET: Got Uzer with id [{}]: {}", dtoUzer.getId(), dtoUzer);
        return dtoUzer;
    }

    @HasRoleAdmin
    @GetMapping("/all/roles")
    public List<String> getRoles() {
        return Arrays.stream(UzerRole.values())
                .map(UzerRole::getValue)
                .sorted()
                .collect(Collectors.toList());
    }

    @HasRoleAny
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateUzer(@PathVariable @Min(1) Integer id,
                                                          @Valid @RequestBody DtoUzerUpdate uzer) {
        log.info("-> PUT: Updating user [{}]: {}", id, uzer);
        Uzer updatedUzer = serviceUzer.updateUzer(id, uzer);
        Map<String, String> response = new HashMap<>();
        response.put("response", "User [" + updatedUzer.getId() + "] was updated");
        log.info("<- PUT: User [{}] was updated", updatedUzer.getId());
        return ResponseEntity.ok(response);
    }

    @HasRoleAdmin
    @PutMapping("/activity/{id}")
    public ResponseEntity<Map<String, String>> updateUzerActivity(@PathVariable @Min(1) Integer id) {
        log.info("-> PUT: Updating user [{}] activity", id);
        Uzer updatedUzer = serviceUzer.updateUzerActivity(id);
        Map<String, String> response = new HashMap<>();
        response.put("response", "User [" + updatedUzer.getId() + "] activity was updated to: " + updatedUzer.getIsActive());
        log.info("<- PUT: User [{}] activity was updated to: {}", updatedUzer.getId(), updatedUzer.getIsActive());
        return ResponseEntity.ok(response);
    }
}