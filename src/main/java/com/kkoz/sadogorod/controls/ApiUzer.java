package com.kkoz.sadogorod.controls;


import com.kkoz.sadogorod.controls.exceptions.NotFoundException;
import com.kkoz.sadogorod.dto.uzer.DtoUzer;
import com.kkoz.sadogorod.dto.uzer.DtoUzerCredentials;
import com.kkoz.sadogorod.dto.uzer.DtoUzerPagination;
import com.kkoz.sadogorod.dto.uzer.DtoUzerUpdate;
import com.kkoz.sadogorod.entities.uzer.Uzer;
import com.kkoz.sadogorod.entities.uzer.UzerRole;
import com.kkoz.sadogorod.services.ServiceUzer;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class ApiUzer {

    private final ServiceUzer serviceUzer;

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

    @GetMapping("/{id}")
    public DtoUzer getUzer(@PathVariable @Min(1) Integer id) {
        log.info("-> GET: Getting Uzer with id [{}]", id);
        DtoUzer dtoUzer = new DtoUzer(serviceUzer.getById(id));
        log.info("<- GET: Got Uzer with id [{}]: {}", dtoUzer.getId(), dtoUzer);
        return dtoUzer;
    }

    @GetMapping("/all/roles")
    public List<String> getRoles() {
        return Arrays.stream(UzerRole.values())
                .map(UzerRole::getValue)
                .sorted()
                .collect(Collectors.toList());
    }

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

    @PutMapping("/activity/{id}")
    public ResponseEntity<Map<String, String>> updateUzerActivity(@PathVariable @Min(1) Integer id) {
        log.info("-> PUT: Updating user [{}] activity", id);
        Uzer updatedUzer = serviceUzer.updateUzerActivity(id);
        Map<String, String> response = new HashMap<>();
        response.put("response", "User [" + updatedUzer.getId() + "] activity was updated to: " + updatedUzer.getIsActive());
        log.info("<- PUT: User [{}] activity was updated to: {}", updatedUzer.getId(), updatedUzer.getIsActive());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current")
    public DtoUzer getCurrentUzer() {
        log.info("-> GET: Getting current Uzer");
        DtoUzer dtoUzer = new DtoUzer(serviceUzer.getCurrentUzer());
        log.info("<- GET: Got current Uzer");
        return dtoUzer;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody DtoUzerCredentials credentials) throws LoginException {
        log.info("-> POST: Authentication: uzername[{}]", credentials.getUsername());

        try {

            final String username = credentials.getUsername();
            final String password = credentials.getPassword();

            Uzer uzer;

            if (serviceUzer.existsByUsername(username)) {
                uzer = serviceUzer.getByCredentials(new DtoUzerCredentials(username, password));
                if (!uzer.getIsActive()) {
                    throw new NotFoundException("Неверно указан логин или пароль");
                }
            }
            else {
                throw new NotFoundException("Неверно указан логин или пароль");
            }
        } catch (NotFoundException e) {
            log.debug("<- POST: Authentication failed. There is no such uzer with uzername[{}]", credentials.getUsername());
            throw new LoginException("Неверно указан логин или пароль");
        }

        if (!serviceUzer.checkActive(credentials.getUsername())) {
            throw new LoginException("Аккаунт не активирован");
        }

        return ResponseEntity.ok("Успешно");
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createUzer(@RequestBody DtoUzer uzer) throws UnirestException {
        log.info("-> POST: Adding new user: {}", uzer);
        Uzer createdUzer = serviceUzer.createUzer(uzer);
        Map<String, String> response = new HashMap<>();
        response.put("id", createdUzer.getId().toString());
        response.put("response", "User created with id [" + createdUzer.getId() + "]");
        log.info("<- POST: User created with id [{}]", createdUzer.getId());
        serviceUzer.mail(uzer);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/activity/{username}/{key}")
    public ResponseEntity<Map<String, String>> updateUzerMailActivity(@PathVariable String username,
                                                                      @PathVariable Integer key) throws LoginException {
        log.info("-> PUT: Updating userMail [{}] activity", username);
        serviceUzer.updateUzerMailActivity(username, key);
        Map<String, String> response = new HashMap<>();
        response.put("response", "UserMail [" + username + "] activity was updated");
        log.info("<- PUT: UserMail [{}] activity was updated", username);
        return ResponseEntity.ok(response);
    }
}
