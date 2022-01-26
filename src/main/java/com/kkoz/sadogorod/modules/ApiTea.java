package com.kkoz.sadogorod.modules;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kkoz.sadogorod.controls.exceptions.MismatchException;
import com.kkoz.sadogorod.controls.exceptions.UniqueUzerException;
import com.kkoz.sadogorod.dto.file.DtoFileUpload;
import com.kkoz.sadogorod.entities.dictionaries.TypeDocument;
import com.kkoz.sadogorod.entities.file.FileUpload;
import com.kkoz.sadogorod.entities.meta.MetaEntityWithFiles;
import com.kkoz.sadogorod.services.ServicePageable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tea")
public class ApiTea {

    public static Boolean active = true;
    ServiceTea serviceTea;

    @GetMapping("/all")
    public ResponseEntity<Page<DtoTeaPagination>> getPage(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                             @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                             @RequestParam(required = false, defaultValue = "-id") String sort) {
        if (!active) {
            return new ResponseEntity<>(new PageImpl<>(new ArrayList<>()), HttpStatus.NOT_FOUND);
        }
        Page<DtoTeaPagination> teaPage = serviceTea.getPage(page, size, sort);
        return new ResponseEntity<>(teaPage, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> create(@RequestBody DtoTea tea) {
        if (!active) {
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_FOUND);
        }
        Tea createdTea = serviceTea.create(tea);
        Map<String, String> response = new HashMap<>();
        response.put("id", createdTea.getId().toString());
        response.put("response", " created with id [" + createdTea.getId() + "]");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<DtoTea> get(@PathVariable @Min(1) Integer id) {
        if (!active) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        DtoTea dtoTea = new DtoTea(serviceTea.getById(id));
        return new ResponseEntity<>(dtoTea, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable @Min(1) Integer id,
                                                      @RequestBody DtoTea tea) {
        if (!active) {
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_FOUND);
        }
        Tea updatedTea = serviceTea.update(id, tea);
        Map<String, String> response = new HashMap<>();
        response.put("response", " [" + updatedTea.getId() + "] was updated");
        return ResponseEntity.ok(response);
    }
}

@Service
@RequiredArgsConstructor
class ServiceTea {

    private final RepoTea repoTea;
    private final ServicePageable servicePageable;

    public Page<DtoTeaPagination> getPage(Integer page, Integer size, String sort) {
        Pageable pageConfig = servicePageable.getPageConfig(page, size, sort);

        Page<Tea> teaPage = repoTea.findAll(pageConfig);

        List<DtoTeaPagination> dtoTeaPagination = teaPage.stream()
                .map(DtoTeaPagination::new)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoTeaPagination, pageConfig, teaPage.getTotalElements());
    }

    public Tea getById(Integer id) {
        return repoTea.getById(id);
    }

    public Tea create(DtoTea dtoTea) {
        Tea tea = new Tea();
        return this.save(tea, dtoTea);
    }

    public Tea update(Integer id, DtoTea dtoTea) {
        if (!id.equals(dtoTea.getId())) {
            throw new MismatchException(
                    "Provided id [" + id + "] isn't equal to provided DTO id [" + dtoTea.getId() + "]"
            );
        }

        Tea tea = repoTea.getById(id);

        return this.save(tea, dtoTea);
    }

    private Tea save(Tea tea, DtoTea dtoTea) {
        tea.setName(dtoTea.getName());
        tea.setDescription(dtoTea.getDescription());
        tea.setFiles(List.of(this.dtoDoc2AppFile(dtoTea.getPhoto(), TypeDocument.TEA_PHOTO)));
        tea.setTime(dtoTea.getTime());

        Tea savedTea;

        try {
            savedTea = repoTea.save(tea);
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException constraintViolation = (ConstraintViolationException) e.getCause();
            throw new UniqueUzerException(constraintViolation.getConstraintName());
        }

        return savedTea;
    }

    private FileUpload dtoDoc2AppFile(DtoFileUpload dtoFile, TypeDocument typeDocument) {
        FileUpload appFile = new FileUpload();

        appFile.setId(dtoFile.getId());
        appFile.setCreated(dtoFile.getCreated());
        appFile.setMimeType(dtoFile.getMimeType());
        appFile.setOriginalFileName(dtoFile.getOriginalFileName());
        appFile.setUuid(dtoFile.getUuid());
        appFile.setSize(dtoFile.getSize());
        appFile.setStorePath(dtoFile.getStorePath());
        appFile.setTypeDocument(typeDocument);

        return appFile;
    }
}

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
class Tea extends MetaEntityWithFiles {

    private String name;
    @Column(columnDefinition="TEXT")
    private String description;
    private Integer time;
}

@Repository
interface RepoTea extends JpaRepository<Tea, Integer> {

}

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class DtoTea {

    private Integer id;
    private String name;
    private String description;
    private DtoFileUpload photo;
    private Integer time;

    public DtoTea(Tea entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.photo = new DtoFileUpload(entity.getFiles().stream().findFirst().get());
        this.time = entity.getTime();
    }
}

@Data
class DtoTeaPagination {

    private Integer id;
    private String name;
    private DtoFileUpload photo;

    public DtoTeaPagination(Tea entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.photo = new DtoFileUpload(entity.getFiles().stream().findFirst().get());
    }

}