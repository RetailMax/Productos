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
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleValidationExceptions() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "producto");
        bindingResult.addError(new FieldError("producto", "name", "no puede estar vacío"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(ex);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("name"));
    }

    @Test
    void testHandleDataIntegrityViolation() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("error", new RuntimeException("detalle"));
        ResponseEntity<Map<String, String>> response = handler.handleDataIntegrityViolation(ex);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().get("error").contains("Error de integridad de datos"));
    }
} 