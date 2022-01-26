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
@RequestMapping("/api/greenhouse")
public class ApiGreenhouse {

    public static Boolean active = true;
    private final ServiceGreenhouse serviceGreenhouse;

    @GetMapping("/all")
    public ResponseEntity<Page<DtoGreenhousePagination>> getPage(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                             @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                             @RequestParam(required = false, defaultValue = "-id") String sort) {
        if (!active) {
            return new ResponseEntity<>(new PageImpl<>(new ArrayList<>()), HttpStatus.NOT_FOUND);
        }
        Page<DtoGreenhousePagination> greenhousePage = serviceGreenhouse.getPage(page, size, sort);
        return new ResponseEntity<>(greenhousePage, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> create(@RequestBody DtoGreenhouse greenhouse) {
        if (!active) {
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_FOUND);
        }
        Greenhouse createdGreenhouse = serviceGreenhouse.create(greenhouse);
        Map<String, String> response = new HashMap<>();
        response.put("id", createdGreenhouse.getId().toString());
        response.put("response", " created with id [" + createdGreenhouse.getId() + "]");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<DtoGreenhouse> get(@PathVariable @Min(1) Integer id) {
        if (!active) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        DtoGreenhouse dtoGreenhouse = new DtoGreenhouse(serviceGreenhouse.getById(id));
        return new ResponseEntity<>(dtoGreenhouse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable @Min(1) Integer id,
                                                      @RequestBody DtoGreenhouse greenhouse) {
        if (!active) {
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_FOUND);
        }
        Greenhouse updatedGreenhouse = serviceGreenhouse.update(id, greenhouse);
        Map<String, String> response = new HashMap<>();
        response.put("response", " [" + updatedGreenhouse.getId() + "] was updated");
        return ResponseEntity.ok(response);
    }
}

@Service
@RequiredArgsConstructor
class ServiceGreenhouse {

    private final RepoGreenhouse repoGreenhouse;
    private final ServicePageable servicePageable;

    public Page<DtoGreenhousePagination> getPage(Integer page, Integer size, String sort) {
        Pageable pageConfig = servicePageable.getPageConfig(page, size, sort);

        Page<Greenhouse> greenhousePage = repoGreenhouse.findAll(pageConfig);

        List<DtoGreenhousePagination> dtoGreenhousePagination = greenhousePage.stream()
                .map(DtoGreenhousePagination::new)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoGreenhousePagination, pageConfig, greenhousePage.getTotalElements());
    }

    public Greenhouse getById(Integer id) {
        return repoGreenhouse.getById(id);
    }

    public Greenhouse create(DtoGreenhouse dtoGreenhouse) {
        Greenhouse greenhouse = new Greenhouse();
        return this.save(greenhouse, dtoGreenhouse);
    }

    public Greenhouse update(Integer id, DtoGreenhouse dtoGreenhouse) {
        if (!id.equals(dtoGreenhouse.getId())) {
            throw new MismatchException(
                    "Provided id [" + id + "] isn't equal to provided DTO id [" + dtoGreenhouse.getId() + "]"
            );
        }

        Greenhouse greenhouse = repoGreenhouse.getById(id);

        return this.save(greenhouse, dtoGreenhouse);
    }

    private Greenhouse save(Greenhouse greenhouse, DtoGreenhouse dtoGreenhouse) {
        greenhouse.setName(dtoGreenhouse.getName());
        greenhouse.setDescription(dtoGreenhouse.getDescription());
        greenhouse.setFiles(List.of(this.dtoDoc2AppFile(dtoGreenhouse.getPhoto(), TypeDocument.GREENHOUSE_PHOTO)));
        greenhouse.setMaterials(dtoGreenhouse.getMaterials());

        Greenhouse savedGreenhouse;

        try {
            savedGreenhouse = repoGreenhouse.save(greenhouse);
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException constraintViolation = (ConstraintViolationException) e.getCause();
            throw new UniqueUzerException(constraintViolation.getConstraintName());
        }

        return savedGreenhouse;
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
class Greenhouse extends MetaEntityWithFiles {

    private String name;
    @Column(columnDefinition="TEXT")
    private String description;
    private String materials;
}

@Repository
interface RepoGreenhouse extends JpaRepository<Greenhouse, Integer> {

}

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class DtoGreenhouse {

    private Integer id;
    private String name;
    private String description;
    private DtoFileUpload photo;
    private String materials;

    public DtoGreenhouse(Greenhouse entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.photo = new DtoFileUpload(entity.getFiles().stream().findFirst().get());
        this.materials = entity.getMaterials();
    }
}

@Data
class DtoGreenhousePagination {

    private Integer id;
    private String name;
    private DtoFileUpload photo;

    public DtoGreenhousePagination(Greenhouse entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.photo = new DtoFileUpload(entity.getFiles().stream().findFirst().get());
    }

}