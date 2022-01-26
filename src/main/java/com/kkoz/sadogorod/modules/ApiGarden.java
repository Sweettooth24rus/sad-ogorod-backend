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
@RequestMapping("/api/garden")
public class ApiGarden {

    public static Boolean active = true;
    ServiceGarden serviceGarden;

    public static void changeActivity() {
        active = !active;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<DtoGardenPagination>> getPage(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                             @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                             @RequestParam(required = false, defaultValue = "-id") String sort) {
        if (!active) {
            return new ResponseEntity<>(new PageImpl<>(new ArrayList<>()), HttpStatus.NOT_FOUND);
        }
        Page<DtoGardenPagination> gardenPage = serviceGarden.getPage(page, size, sort);
        return new ResponseEntity<>(gardenPage, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> create(@RequestBody DtoGarden garden) {
        if (!active) {
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_FOUND);
        }
        Garden createdGarden = serviceGarden.create(garden);
        Map<String, String> response = new HashMap<>();
        response.put("id", createdGarden.getId().toString());
        response.put("response", " created with id [" + createdGarden.getId() + "]");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<DtoGarden> get(@PathVariable @Min(1) Integer id) {
        if (!active) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        DtoGarden dtoGarden = new DtoGarden(serviceGarden.getById(id));
        return new ResponseEntity<>(dtoGarden, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable @Min(1) Integer id,
                                                      @RequestBody DtoGarden garden) {
        if (!active) {
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_FOUND);
        }
        Garden updatedGarden = serviceGarden.update(id, garden);
        Map<String, String> response = new HashMap<>();
        response.put("response", " [" + updatedGarden.getId() + "] was updated");
        return ResponseEntity.ok(response);
    }
}

@Service
@RequiredArgsConstructor
class ServiceGarden {

    private final RepoGarden repoGarden;
    private final ServicePageable servicePageable;

    public Page<DtoGardenPagination> getPage(Integer page, Integer size, String sort) {
        Pageable pageConfig = servicePageable.getPageConfig(page, size, sort);

        Page<Garden> gardenPage = repoGarden.findAll(pageConfig);

        List<DtoGardenPagination> dtoGardenPagination = gardenPage.stream()
                .map(DtoGardenPagination::new)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoGardenPagination, pageConfig, gardenPage.getTotalElements());
    }

    public Garden getById(Integer id) {
        return repoGarden.getById(id);
    }

    public Garden create(DtoGarden dtoGarden) {
        Garden garden = new Garden();
        return this.save(garden, dtoGarden);
    }

    public Garden update(Integer id, DtoGarden dtoGarden) {
        if (!id.equals(dtoGarden.getId())) {
            throw new MismatchException(
                    "Provided id [" + id + "] isn't equal to provided DTO id [" + dtoGarden.getId() + "]"
            );
        }

        Garden garden = repoGarden.getById(id);

        return this.save(garden, dtoGarden);
    }

    private Garden save(Garden garden, DtoGarden dtoGarden) {
        garden.setName(dtoGarden.getName());
        garden.setDescription(dtoGarden.getDescription());
        garden.setFiles(List.of(this.dtoDoc2AppFile(dtoGarden.getPhoto(), TypeDocument.GARDEN_PHOTO)));
        garden.setSquare(dtoGarden.getSquare());

        Garden savedGarden;

        try {
            savedGarden = repoGarden.save(garden);
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException constraintViolation = (ConstraintViolationException) e.getCause();
            throw new UniqueUzerException(constraintViolation.getConstraintName());
        }

        return savedGarden;
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
class Garden extends MetaEntityWithFiles {

    private String name;
    @Column(columnDefinition="TEXT")
    private String description;
    private String square;
}

@Repository
interface RepoGarden extends JpaRepository<Garden, Integer> {

}

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class DtoGarden {

    private Integer id;
    private String name;
    private String description;
    private DtoFileUpload photo;
    private String square;

    public DtoGarden(Garden entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.photo = new DtoFileUpload(entity.getFiles().stream().findFirst().get());
        this.square = entity.getSquare();
    }
}

@Data
class DtoGardenPagination {

    private Integer id;
    private String name;
    private DtoFileUpload photo;
    private String square;

    public DtoGardenPagination(Garden entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.photo = new DtoFileUpload(entity.getFiles().stream().findFirst().get());
        this.square = entity.getSquare();
    }

}