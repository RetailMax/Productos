package com.duoc.productos.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class GlobalExceptionHandlerTest {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleValidationExceptions() {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new Object(), "obj");
        errors.addError(new FieldError("obj", "campo", "mensaje"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, errors);
        ResponseEntity<Map<String, String>> resp = handler.handleValidationExceptions(ex);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().containsKey("campo"));
    }

    @Test
    void testHandleDataIntegrityViolation() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("error", new RuntimeException("detalle"));
        ResponseEntity<Map<String, String>> resp = handler.handleDataIntegrityViolation(ex);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().get("error").contains("integridad"));
    }
} 