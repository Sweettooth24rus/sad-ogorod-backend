package com.kkoz.sadogorod.controls;


import com.kkoz.sadogorod.dto.recipe.DtoRecipe;
import com.kkoz.sadogorod.dto.recipe.DtoRecipePagination;
import com.kkoz.sadogorod.dto.recipe.DtoRecipeShow;
import com.kkoz.sadogorod.entities.recipe.Recipe;
import com.kkoz.sadogorod.services.ServiceRecipe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe")
public class ApiRecipe {

    private final ServiceRecipe serviceRecipe;

    @Transactional
    @GetMapping("/all")
    public Page<DtoRecipePagination> getPage(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                             @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                             @RequestParam(required = false, defaultValue = "-id") String sort,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) Integer difficulty) {
        log.info("-> GET: Getting recipe page (page: {}, size: {}, sort: {}, name: {}, difficulty: {}",
                page, size, sort, name, difficulty);
        Page<DtoRecipePagination> recipePage = serviceRecipe.getPage(page, size, sort, name, difficulty);
        log.info("<- GET: Got recipe page (page: {}, size: {}, sort: {}, name: {}, difficulty: {}",
                page, size, sort, name, difficulty);
        return recipePage;
    }

    @Transactional
    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createRecipe(@RequestBody DtoRecipe recipe) {
        log.info("-> POST: Adding new recipe: {}", recipe);
        Recipe createdRecipe = serviceRecipe.createRecipe(recipe);
        Map<String, String> response = new HashMap<>();
        response.put("id", createdRecipe.getId().toString());
        response.put("response", "Recipe created with id [" + createdRecipe.getId() + "]");
        log.info("<- POST: Recipe created with id [{}]", createdRecipe.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping("/{id}")
    public DtoRecipeShow getRecipe(@PathVariable @Min(1) Integer id) {
        log.info("-> GET: Getting Recipe with id [{}]", id);
        DtoRecipeShow dtoRecipe = new DtoRecipeShow(serviceRecipe.getById(id));
        log.info("<- GET: Got Recipe with id [{}]: {}", dtoRecipe.getId(), dtoRecipe);
        return dtoRecipe;
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateRecipe(@PathVariable @Min(1) Integer id,
                                                          @Valid @RequestBody DtoRecipe recipe) {
        log.info("-> PUT: Updating recipe [{}]: {}", id, recipe);
        Recipe updatedRecipe = serviceRecipe.updateRecipe(id, recipe);
        Map<String, String> response = new HashMap<>();
        response.put("response", "Recipe [" + updatedRecipe.getId() + "] was updated");
        log.info("<- PUT: Recipe [{}] was updated", updatedRecipe.getId());
        return ResponseEntity.ok(response);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable @Min(1) Integer id) {
        return ResponseEntity.ok(serviceRecipe.deleteRecipe(id));
    }
}
