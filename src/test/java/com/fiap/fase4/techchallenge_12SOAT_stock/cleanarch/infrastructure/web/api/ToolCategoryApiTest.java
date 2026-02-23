package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.api;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.controller.ToolCategoryController;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.ToolCategoryRequestDTO;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolCategoryApiTest {

    @Mock
    private ToolCategoryController controller;

    @InjectMocks
    private ToolCategoryApi api;

    @Test
    void createToolCategory_Success() {
        ToolCategoryRequestDTO req = new ToolCategoryRequestDTO("Test");
        ToolCategoryResponseDTO resp = ToolCategoryResponseDTO.builder().build();
        when(controller.createToolCategory(req)).thenReturn(resp);

        ResponseEntity<ToolCategoryResponseDTO> result = api.createToolCategory(req);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(resp, result.getBody());
    }

    @Test
    void createToolCategory_BadRequest() {
        when(controller.createToolCategory(any())).thenThrow(new IllegalArgumentException("Error"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> api.createToolCategory(new ToolCategoryRequestDTO()));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void getToolCategoryById() {
        UUID id = UUID.randomUUID();
        ToolCategoryResponseDTO resp = ToolCategoryResponseDTO.builder().build();
        when(controller.getToolCategoryById(id)).thenReturn(resp);

        assertEquals(resp, api.getToolCategoryById(id));
    }

    @Test
    void getAllToolCategories() {
        List<ToolCategoryResponseDTO> list = List.of(ToolCategoryResponseDTO.builder().build());
        when(controller.getAllToolCategories()).thenReturn(list);
        assertEquals(list, api.getAllToolCategories());
    }

    @Test
    void getAllToolCategoriesActive() {
        List<ToolCategoryResponseDTO> list = List.of(ToolCategoryResponseDTO.builder().build());
        when(controller.getAllToolCategoriesActive()).thenReturn(list);
        assertEquals(list, api.getAllToolCategoriesActive());
    }

    @Test
    void updateToolCategory_Success() {
        UUID id = UUID.randomUUID();
        ToolCategoryRequestDTO req = new ToolCategoryRequestDTO("A");
        ToolCategoryResponseDTO resp = ToolCategoryResponseDTO.builder().build();
        when(controller.updateToolCategory(id, req)).thenReturn(resp);

        assertEquals(resp, api.updateToolCategory(id, req));
    }

    @Test
    void updateToolCategory_BadRequest() {
        UUID id = UUID.randomUUID();
        when(controller.updateToolCategory(eq(id), any())).thenThrow(new IllegalArgumentException("Error"));

        assertThrows(ResponseStatusException.class, () -> api.updateToolCategory(id, new ToolCategoryRequestDTO()));
    }

    @Test
    void deleteToolCategory() {
        UUID id = UUID.randomUUID();
        api.deleteToolCategory(id);
        verify(controller).logicallyDeleteToolCategory(id);
    }

    @Test
    void reactivateToolCategory() {
        UUID id = UUID.randomUUID();
        ToolCategoryResponseDTO resp = ToolCategoryResponseDTO.builder().build();
        when(controller.reactivateToolCategory(id)).thenReturn(resp);

        assertEquals(resp, api.reactivateToolCategory(id));
    }
}
