package com.kkoz.sadogorod.services;

import com.kkoz.sadogorod.controls.exceptions.MismatchException;
import com.kkoz.sadogorod.controls.exceptions.NotFoundException;
import com.kkoz.sadogorod.controls.exceptions.UniqueUzerException;
import com.kkoz.sadogorod.dto.uzer.DtoUzer;
import com.kkoz.sadogorod.dto.uzer.DtoUzerCredentials;
import com.kkoz.sadogorod.dto.uzer.DtoUzerPagination;
import com.kkoz.sadogorod.dto.uzer.DtoUzerUpdate;
import com.kkoz.sadogorod.entities.uzer.Uzer;
import com.kkoz.sadogorod.entities.uzer.UzerMail;
import com.kkoz.sadogorod.entities.uzer.UzerRole;
import com.kkoz.sadogorod.filters.SpecUzer;
import com.kkoz.sadogorod.repositories.RepoUzer;
import com.kkoz.sadogorod.repositories.RepoUzerMail;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceUzer {

    private final RepoUzerMail repoUzerMail;
    private final RepoUzer repoUzer;
    private final SpecUzer specUzer;
    private final ServicePageable servicePageable;
    private static Uzer currentUzer;

    @SneakyThrows
    public Uzer loadUserByUsername(String username) {
        return repoUzer.getByUsername(username)
                .orElseThrow(() -> new LoginException(
                        "Неверно указан логин или пароль"
                ));
    }

    @SneakyThrows
    public Uzer getByUsername(String username) {
        return repoUzer.getByUsername(username)
                .orElseThrow(() -> new LoginException(
                        "Неверно указан логин или пароль"
                ));
    }

    public Uzer getByCredentials(DtoUzerCredentials credentials) {
        String encodedPassword = this.getByUsername(credentials.getUsername()).getPassword();
        if (credentials.getPassword().equals(encodedPassword)) {
            Uzer tmp = repoUzer.getByUsername(credentials.getUsername()).get();
            currentUzer = tmp;
            return tmp;
        }
        else {
            throw new NotFoundException("Неправильно указан логин или пароль");
        }
    }

    public Uzer getCurrentUzer() {
        return currentUzer;
    }

    public Page<DtoUzerPagination> getPage(Integer page, Integer size, String sort, String username,
                                           String lastName, String firstName, String patronymicName,
                                           String role, String isActive) {
        Pageable pageConfig = servicePageable.getPageConfig(page, size, sort);
        //TODO Фильтрации и сортировки
        Specification spec = Specification.where(null);

        if (username != null) {
            spec = spec.and(specUzer.getUsernameFilter(username));
        }

        if (firstName != null) {
            spec = spec.and(specUzer.getFirstNameFilter(firstName));
        }

        if (lastName != null) {
            spec = spec.and(specUzer.getLastNameFilter(lastName));
        }

        if (patronymicName != null) {
            spec = spec.and(specUzer.getPatronymicNameFilter(patronymicName));
        }

        if (role != null) {
            spec = spec.and(specUzer.getRoleFilter(UzerRole.getRoleFromKey(role)));
        }

        if (isActive != null) {
            if (isActive.equals("ACTIVE"))
                spec = spec.and(specUzer.getIsActiveFilter(true));
            else
                spec = spec.and(specUzer.getIsActiveFilter(false));
        }

        Page<Uzer> uzerPage = repoUzer.findAll(spec, pageConfig);

        List<DtoUzerPagination> dtoUzerPagination = uzerPage.stream()
                .map(DtoUzerPagination::new)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoUzerPagination, pageConfig, uzerPage.getTotalElements());
    }

    public Uzer getById(Integer id) {
        return repoUzer.getUzerById(id).orElseThrow(
                () -> new NotFoundException(id, Uzer.class.getSimpleName())
        );
    }

    public Uzer createUzer(DtoUzer dtoUzer) {
        if (repoUzer.existsByUsername(dtoUzer.getUsername())) {
            return null;
        }
        Uzer uzer = new Uzer();
        return this.saveUzer(uzer, new DtoUzerUpdate(dtoUzer));
    }

    public Uzer updateUzer(Integer id, DtoUzerUpdate dtoUzer) {
        if (!id.equals(dtoUzer.getId())) {
            throw new MismatchException(
                    "Provided id [" + id + "] isn't equal to provided DTO id [" + dtoUzer.getId() + "]"
            );
        }

        Uzer uzer = repoUzer.getUzerById(id).orElseThrow(
                () -> new NotFoundException(id, Uzer.class.getSimpleName())
        );

        return this.saveUzer(uzer, dtoUzer);
    }

    private Uzer saveUzer(Uzer uzer, DtoUzerUpdate dtoUzer) {
        uzer.setUsername(dtoUzer.getUsername());

        if (dtoUzer.getPassword() != null && !dtoUzer.getPassword().isBlank()) {
            uzer.setPassword(
                    dtoUzer.getPassword()
            );
        }
        uzer.setLastName(dtoUzer.getLastName());
        uzer.setFirstName(dtoUzer.getFirstName());
        uzer.setPatronymicName(dtoUzer.getPatronymicName());
        uzer.setEmail(dtoUzer.getEmail());
        uzer.setPhone(dtoUzer.getPhone());
        uzer.setRole(UzerRole.ORDINARY);
        uzer.setIsActive(true);

        Uzer savedUzer;

        try {
            savedUzer = repoUzer.save(uzer);
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException constraintViolation = (ConstraintViolationException) e.getCause();
            throw new UniqueUzerException(constraintViolation.getConstraintName());
        }

        return savedUzer;
    }

    public Uzer updateUzerActivity(Integer id) {
        Uzer uzer = repoUzer.getUzerById(id).orElseThrow(
                () -> new NotFoundException(id, Uzer.class.getSimpleName())
        );

        uzer.setIsActive(!uzer.getIsActive());

        return repoUzer.save(uzer);
    }

    public boolean existsByUsername(String username) {
        return repoUzer.existsByUsername(username);
    }

    public boolean checkActive(String username) {
        return repoUzerMail.getByUsername(username).get().getActive();
    }

    public JsonNode mail(DtoUzer uzer) throws UnirestException {
        UzerMail uzerMail = new UzerMail();
        uzerMail.setUsername(uzer.getUsername());
        uzerMail.setActive(false);
        repoUzerMail.save(uzerMail);
        String text;
        text = "Здравствуй. Необходимо подтвердить аккаунт. " +
                "Для этого необходимо перейти по ссылке:\n " +
                "https://sad-ogorod.herokuapp.com/api/user/activity/"
                + uzerMail.getUsername() + "/" + uzerMail.getId() ;
        HttpResponse<JsonNode> request = Unirest.post(System.getenv("MAIL_GUN_DOMAIN") + "/messages")
                .basicAuth("api", System.getenv("MAIL_GUN_KEY"))
                .queryString("from", "sad-ogorod <System@kkoz.sadogorod.com>")
                .queryString("to", uzer.getEmail())
                .queryString("subject", "Подтверждение аккаунта")
                .queryString("text", text)
                .asJson();
        return request.getBody();
    }

    public void updateUzerMailActivity(String username, Integer key) throws LoginException {
        UzerMail uzerMail = repoUzerMail.getByUsername(username).orElseThrow(
                () -> new NotFoundException(username, UzerMail.class.getSimpleName())
        );

        if (!uzerMail.getId().equals(key)) {
            throw new LoginException("Логин и ключ не соотносятся");
        }

        uzerMail.setActive(true);
        repoUzerMail.save(uzerMail);
    }
}
