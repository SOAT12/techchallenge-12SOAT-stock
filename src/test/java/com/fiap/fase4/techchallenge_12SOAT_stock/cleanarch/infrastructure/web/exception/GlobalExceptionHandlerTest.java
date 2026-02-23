package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.exception;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleNotFoundException() {
        NotFoundException ex = new NotFoundException("Not found error");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleNotFoundException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found error", response.getBody().get("message"));
    }

    @Test
    void handleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Illegal argument");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleIllegalArgument(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Illegal argument", response.getBody().get("message"));
    }

    @Test
    void handleIllegalState() {
        IllegalStateException ex = new IllegalStateException("Illegal state");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleIllegalState(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Illegal state", response.getBody().get("message"));
    }
}
