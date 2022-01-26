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
@RequestMapping("/api/pickle")
public class ApiPickle {

    public static Boolean active = true;
    private final ServicePickle servicePickle;

    @GetMapping("/all")
    public ResponseEntity<Page<DtoPicklePagination>> getPage(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                             @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                             @RequestParam(required = false, defaultValue = "-id") String sort) {
        if (!active) {
            return new ResponseEntity<>(new PageImpl<>(new ArrayList<>()), HttpStatus.NOT_FOUND);
        }
        Page<DtoPicklePagination> picklePage = servicePickle.getPage(page, size, sort);
        return new ResponseEntity<>(picklePage, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> create(@RequestBody DtoPickle pickle) {
        if (!active) {
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_FOUND);
        }
        Pickle createdPickle = servicePickle.create(pickle);
        Map<String, String> response = new HashMap<>();
        response.put("id", createdPickle.getId().toString());
        response.put("response", " created with id [" + createdPickle.getId() + "]");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<DtoPickle> get(@PathVariable @Min(1) Integer id) {
        if (!active) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        DtoPickle dtoPickle = new DtoPickle(servicePickle.getById(id));
        return new ResponseEntity<>(dtoPickle, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable @Min(1) Integer id,
                                                      @RequestBody DtoPickle pickle) {
        if (!active) {
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_FOUND);
        }
        Pickle updatedPickle = servicePickle.update(id, pickle);
        Map<String, String> response = new HashMap<>();
        response.put("response", " [" + updatedPickle.getId() + "] was updated");
        return ResponseEntity.ok(response);
    }
}

@Service
@RequiredArgsConstructor
class ServicePickle {

    private final RepoPickle repoPickle;
    private final ServicePageable servicePageable;

    public Page<DtoPicklePagination> getPage(Integer page, Integer size, String sort) {
        Pageable pageConfig = servicePageable.getPageConfig(page, size, sort);

        Page<Pickle> picklePage = repoPickle.findAll(pageConfig);

        List<DtoPicklePagination> dtoPicklePagination = picklePage.stream()
                .map(DtoPicklePagination::new)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoPicklePagination, pageConfig, picklePage.getTotalElements());
    }

    public Pickle getById(Integer id) {
        return repoPickle.getById(id);
    }

    public Pickle create(DtoPickle dtoPickle) {
        Pickle pickle = new Pickle();
        return this.save(pickle, dtoPickle);
    }

    public Pickle update(Integer id, DtoPickle dtoPickle) {
        if (!id.equals(dtoPickle.getId())) {
            throw new MismatchException(
                    "Provided id [" + id + "] isn't equal to provided DTO id [" + dtoPickle.getId() + "]"
            );
        }

        Pickle pickle = repoPickle.getById(id);

        return this.save(pickle, dtoPickle);
    }

    private Pickle save(Pickle pickle, DtoPickle dtoPickle) {
        pickle.setName(dtoPickle.getName());
        pickle.setDescription(dtoPickle.getDescription());
        pickle.setFiles(List.of(this.dtoDoc2AppFile(dtoPickle.getPhoto(), TypeDocument.PICKLE_PHOTO)));

        Pickle savedPickle;

        try {
            savedPickle = repoPickle.save(pickle);
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException constraintViolation = (ConstraintViolationException) e.getCause();
            throw new UniqueUzerException(constraintViolation.getConstraintName());
        }

        return savedPickle;
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
class Pickle extends MetaEntityWithFiles {

    private String name;
    @Column(columnDefinition="TEXT")
    private String description;
}

@Repository
interface RepoPickle extends JpaRepository<Pickle, Integer> {

}

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class DtoPickle {

    private Integer id;
    private String name;
    private String description;
    private DtoFileUpload photo;

    public DtoPickle(Pickle entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.photo = new DtoFileUpload(entity.getFiles().stream().findFirst().get());
    }
}

@Data
class DtoPicklePagination {

    private Integer id;
    private String name;
    private DtoFileUpload photo;

    public DtoPicklePagination(Pickle entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.photo = new DtoFileUpload(entity.getFiles().stream().findFirst().get());
    }

}