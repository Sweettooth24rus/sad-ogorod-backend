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
@RequestMapping("/api/weed")
public class ApiWeed {

    public static Boolean active = true;
    private final ServiceWeed serviceWeed;

    @GetMapping("/all")
    public ResponseEntity<Page<DtoWeedPagination>> getPage(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                             @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                             @RequestParam(required = false, defaultValue = "-id") String sort) {
        if (!active) {
            return new ResponseEntity<>(new PageImpl<>(new ArrayList<>()), HttpStatus.NOT_FOUND);
        }
        Page<DtoWeedPagination> weedPage = serviceWeed.getPage(page, size, sort);
        return new ResponseEntity<>(weedPage, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> create(@RequestBody DtoWeed weed) {
        if (!active) {
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_FOUND);
        }
        Weed createdWeed = serviceWeed.create(weed);
        Map<String, String> response = new HashMap<>();
        response.put("id", createdWeed.getId().toString());
        response.put("response", " created with id [" + createdWeed.getId() + "]");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<DtoWeed> get(@PathVariable @Min(1) Integer id) {
        if (!active) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        DtoWeed dtoWeed = new DtoWeed(serviceWeed.getById(id));
        return new ResponseEntity<>(dtoWeed, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable @Min(1) Integer id,
                                                      @RequestBody DtoWeed weed) {
        if (!active) {
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_FOUND);
        }
        Weed updatedWeed = serviceWeed.update(id, weed);
        Map<String, String> response = new HashMap<>();
        response.put("response", " [" + updatedWeed.getId() + "] was updated");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable @Min(1) Integer id) {
        if (!active) {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(serviceWeed.delete(id));
    }
}

@Service
@RequiredArgsConstructor
class ServiceWeed {

    private final RepoWeed repoWeed;
    private final ServicePageable servicePageable;

    public Page<DtoWeedPagination> getPage(Integer page, Integer size, String sort) {
        Pageable pageConfig = servicePageable.getPageConfig(page, size, sort);

        Page<Weed> weedPage = repoWeed.findAll(pageConfig);

        List<DtoWeedPagination> dtoWeedPagination = weedPage.stream()
                .map(DtoWeedPagination::new)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoWeedPagination, pageConfig, weedPage.getTotalElements());
    }

    public Weed getById(Integer id) {
        return repoWeed.getById(id);
    }

    public String delete(Integer id) {
        repoWeed.deleteById(id);
        return "deleted witch id " + id;
    }

    public Weed create(DtoWeed dtoWeed) {
        Weed weed = new Weed();
        return this.save(weed, dtoWeed);
    }

    public Weed update(Integer id, DtoWeed dtoWeed) {
        if (!id.equals(dtoWeed.getId())) {
            throw new MismatchException(
                    "Provided id [" + id + "] isn't equal to provided DTO id [" + dtoWeed.getId() + "]"
            );
        }

        Weed weed = repoWeed.getById(id);

        return this.save(weed, dtoWeed);
    }

    private Weed save(Weed weed, DtoWeed dtoWeed) {
        weed.setName(dtoWeed.getName());
        weed.setDescription(dtoWeed.getDescription());
        weed.setFiles(List.of(this.dtoDoc2AppFile(dtoWeed.getPhoto(), TypeDocument.WEED_PHOTO)));
        weed.setRegion(dtoWeed.getRegion());

        Weed savedWeed;

        try {
            savedWeed = repoWeed.save(weed);
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException constraintViolation = (ConstraintViolationException) e.getCause();
            throw new UniqueUzerException(constraintViolation.getConstraintName());
        }

        return savedWeed;
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
class Weed extends MetaEntityWithFiles {

    private String name;
    @Column(columnDefinition="TEXT")
    private String description;
    private String region;
}

@Repository
interface RepoWeed extends JpaRepository<Weed, Integer> {

}

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class DtoWeed {

    private Integer id;
    private String name;
    private String description;
    private DtoFileUpload photo;
    private String region;

    public DtoWeed(Weed entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.photo = new DtoFileUpload(entity.getFiles().stream().findFirst().get());
        this.region = entity.getRegion();
    }
}

@Data
class DtoWeedPagination {

    private Integer id;
    private String name;
    private DtoFileUpload photo;

    public DtoWeedPagination(Weed entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.photo = new DtoFileUpload(entity.getFiles().stream().findFirst().get());
    }

}