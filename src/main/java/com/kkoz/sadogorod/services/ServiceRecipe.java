package com.kkoz.sadogorod.services;

import com.kkoz.sadogorod.controls.exceptions.MismatchException;
import com.kkoz.sadogorod.controls.exceptions.UniqueUzerException;
import com.kkoz.sadogorod.dto.file.DtoFileUpload;
import com.kkoz.sadogorod.dto.recipe.DtoRecipe;
import com.kkoz.sadogorod.dto.recipe.DtoRecipePagination;
import com.kkoz.sadogorod.entities.dictionaries.TypeDocument;
import com.kkoz.sadogorod.entities.file.FileUpload;
import com.kkoz.sadogorod.entities.recipe.Recipe;
import com.kkoz.sadogorod.filters.SpecRecipe;
import com.kkoz.sadogorod.repositories.RepoDifficulty;
import com.kkoz.sadogorod.repositories.RepoGroundType;
import com.kkoz.sadogorod.repositories.RepoLightType;
import com.kkoz.sadogorod.repositories.RepoRecipe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceRecipe {

    private final RepoRecipe repoRecipe;
    private final ServicePageable servicePageable;
    private final SpecRecipe specRecipe;
    private final RepoGroundType repoGroundType;
    private final RepoLightType repoLightType;
    private final RepoDifficulty repoDifficulty;

    public Page<DtoRecipePagination> getPage(Integer page, Integer size, String sort, String name,
                                             Integer difficulty) {
        Pageable pageConfig = servicePageable.getPageConfig(page, size, sort);
        //TODO Фильтрации и сортировки
        Specification spec = Specification.where(null);

        if (name != null) {
            spec = spec.and(specRecipe.getNameFilter(name));
        }

        if (difficulty != null) {
            spec = spec.and(specRecipe.getDifficultyFilter(repoDifficulty.getById(difficulty)));
        }

        Page<Recipe> recipePage = repoRecipe.findAll(spec, pageConfig);

        List<DtoRecipePagination> dtoRecipePagination = recipePage.stream()
                .map(DtoRecipePagination::new)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoRecipePagination, pageConfig, recipePage.getTotalElements());
    }

    public Recipe getById(Integer id) {
        return repoRecipe.getById(id);
    }

    public String deleteRecipe(Integer id) {
        repoRecipe.deleteById(id);
        return "deleted witch id " + id;
    }

    public Recipe createRecipe(DtoRecipe dtoRecipe) {
        Recipe recipe = new Recipe();
        return this.saveRecipe(recipe, dtoRecipe);
    }

    public Recipe updateRecipe(Integer id, DtoRecipe dtoRecipe) {
        if (!id.equals(dtoRecipe.getId())) {
            throw new MismatchException(
                    "Provided id [" + id + "] isn't equal to provided DTO id [" + dtoRecipe.getId() + "]"
            );
        }

        Recipe recipe = repoRecipe.getById(id);

        return this.saveRecipe(recipe, dtoRecipe);
    }

    private Recipe saveRecipe(Recipe recipe, DtoRecipe dtoRecipe) {
        recipe.setName(dtoRecipe.getName());
        recipe.setDescription(dtoRecipe.getDescription());
        recipe.setFiles(this.dtoDoc2AppFile(dtoRecipe.getPhoto(), TypeDocument.RECIPE_PHOTO));
        recipe.setDays(dtoRecipe.getDays());
        recipe.setLightType(repoLightType.getById(dtoRecipe.getLightType()));
        recipe.setLightTime(dtoRecipe.getLightTime());
        recipe.setGroundType(repoGroundType.getById(dtoRecipe.getGroundType()));
        recipe.setMinTemperature(dtoRecipe.getMinTemperature());
        recipe.setMaxTemperature(dtoRecipe.getMaxTemperature());
        recipe.setDifficulty(repoDifficulty.getById(dtoRecipe.getDifficulty()));
        recipe.setComment(dtoRecipe.getComment());

        Recipe savedRecipe;

        try {
            savedRecipe = repoRecipe.save(recipe);
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException constraintViolation = (ConstraintViolationException) e.getCause();
            throw new UniqueUzerException(constraintViolation.getConstraintName());
        }

        return savedRecipe;
    }

    private List<FileUpload> dtoDoc2AppFile(DtoFileUpload dtoFile, TypeDocument typeDocument) {
        List<FileUpload> appFiles = new ArrayList<>();

        FileUpload appFile = new FileUpload();
        appFile.setId(dtoFile.getId());
        appFile.setCreated(dtoFile.getCreated());
        appFile.setMimeType(dtoFile.getMimeType());
        appFile.setOriginalFileName(dtoFile.getOriginalFileName());
        appFile.setUuid(dtoFile.getUuid());
        appFile.setSize(dtoFile.getSize());
        appFile.setStorePath(dtoFile.getStorePath());
        appFile.setTypeDocument(typeDocument);

        appFiles.add(appFile);

        return appFiles;
    }
}
