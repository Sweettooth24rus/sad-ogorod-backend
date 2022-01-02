package com.kkoz.sadogorod.controls.exceptions.exception_advice;

import com.kkoz.sadogorod.controls.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<Map<String, String>> notFoundHandler(NotFoundException ex) {
        Map<String, String> e = new HashMap<>();
        log.error(ex.getMessage());
        e.put("error", ex.getClass().getSimpleName());
        e.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Map<String, String>> apiException(ConstraintViolationException ex) {
        Map<String, String> e = new HashMap<>();

        log.error(ex.getMessage());
        e.put("error", ex.getClass().getSimpleName());

        var errors = ex.getConstraintViolations();
        if (errors.size() == 1) {
            PathImpl errorParameter = (PathImpl) errors.iterator().next().getPropertyPath();
            e.put("message", errorParameter.getLeafNode().getName() + " " + errors.iterator().next().getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }

        int i = 1;
        for (ConstraintViolation cv : errors) {
            PathImpl errorParameter = (PathImpl) cv.getPropertyPath();
            e.put("message_" + i++, errorParameter.getLeafNode().getName() + " " + cv.getMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> methodArgumentValidationHandler(MethodArgumentNotValidException ex) {
        Map<String, String> e = new HashMap<>();

        e.put("error", ex.getClass().getSimpleName());

        List<ObjectError> errors = ex.getBindingResult().getAllErrors();

        if (errors.size() == 1) {
            log.error(errors.iterator().next().getDefaultMessage());
            e.put("message", errors.iterator().next().getDefaultMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }

        int i = 1;
        for (ObjectError error : errors) {
            log.error(error.getDefaultMessage());
            e.put("message_" + i++, error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    }

    @ResponseBody
    @ExceptionHandler(FileSizeLimitExceededException.class)
    ResponseEntity<Map<String, String>> fileSizeOneHandler(FileSizeLimitExceededException ex) {
        Map<String, String> e = new HashMap<>();
        log.error(ex.getMessage());
        e.put("error", ex.getClass().getSimpleName());
        e.put("message", "Размер файла превышает 50Мб");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    }

    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    ResponseEntity<Map<String, String>> fileSizeTwoHandler(MaxUploadSizeExceededException ex) {
        Map<String, String> e = new HashMap<>();
        log.error(ex.getMessage());
        e.put("error", ex.getClass().getSimpleName());
        e.put("message", "Размер файла превышает 50Мб");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    }

    @ResponseBody
    @ExceptionHandler(SizeLimitExceededException.class)
    ResponseEntity<Map<String, String>> fileSizeThreeHandler(SizeLimitExceededException ex) {
        Map<String, String> e = new HashMap<>();
        log.error(ex.getMessage());
        e.put("error", ex.getClass().getSimpleName());
        e.put("message", "Размер файла превышает 50Мб");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    }
}
