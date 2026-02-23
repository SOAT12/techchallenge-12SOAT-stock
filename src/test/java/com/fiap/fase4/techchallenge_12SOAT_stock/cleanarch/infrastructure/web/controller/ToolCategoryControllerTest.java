package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.controller;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.useCases.ToolCategoryUseCase;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.ToolCategoryPresenter;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.ToolCategoryRequestDTO;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolCategoryControllerTest {

    @Mock
    private ToolCategoryUseCase toolCategoryUseCase;

    @Mock
    private ToolCategoryPresenter toolCategoryPresenter;

    @InjectMocks
    private ToolCategoryController toolCategoryController;

    @Test
    void createToolCategory() {
        ToolCategoryRequestDTO dto = new ToolCategoryRequestDTO();
        dto.setToolCategoryName("Categoria1");

        ToolCategory mockCategory = ToolCategory.create("Categoria1");
        ToolCategoryResponseDTO responseDTO = ToolCategoryResponseDTO.builder()
                .toolCategoryName("Categoria1").build();

        when(toolCategoryUseCase.createToolCategory("Categoria1")).thenReturn(mockCategory);
        when(toolCategoryPresenter.toToolCategoryResponseDTO(mockCategory)).thenReturn(responseDTO);

        ToolCategoryResponseDTO result = toolCategoryController.createToolCategory(dto);

        assertNotNull(result);
        assertEquals("Categoria1", result.getToolCategoryName());
        verify(toolCategoryUseCase).createToolCategory("Categoria1");
    }

    @Test
    void getToolCategoryById() {
        UUID id = UUID.randomUUID();
        ToolCategory mockCategory = ToolCategory.create("Cat");
        when(toolCategoryUseCase.getToolCategoryById(id)).thenReturn(mockCategory);
        when(toolCategoryPresenter.toToolCategoryResponseDTO(mockCategory))
                .thenReturn(ToolCategoryResponseDTO.builder().build());

        ToolCategoryResponseDTO result = toolCategoryController.getToolCategoryById(id);

        assertNotNull(result);
        verify(toolCategoryUseCase).getToolCategoryById(id);
    }

    @Test
    void getAllToolCategories() {
        ToolCategory mockCategory = ToolCategory.create("Cat");
        when(toolCategoryUseCase.getAllToolCategories()).thenReturn(List.of(mockCategory));
        when(toolCategoryPresenter.toToolCategoryResponseDTO(mockCategory))
                .thenReturn(ToolCategoryResponseDTO.builder().build());

        List<ToolCategoryResponseDTO> result = toolCategoryController.getAllToolCategories();

        assertEquals(1, result.size());
        verify(toolCategoryUseCase).getAllToolCategories();
    }

    @Test
    void getAllToolCategoriesActive() {
        ToolCategory mockCategory = ToolCategory.create("Cat");
        when(toolCategoryUseCase.getAllToolCategoriesActive()).thenReturn(List.of(mockCategory));
        when(toolCategoryPresenter.toToolCategoryResponseDTO(mockCategory))
                .thenReturn(ToolCategoryResponseDTO.builder().build());

        List<ToolCategoryResponseDTO> result = toolCategoryController.getAllToolCategoriesActive();

        assertEquals(1, result.size());
        verify(toolCategoryUseCase).getAllToolCategoriesActive();
    }

    @Test
    void updateToolCategory() {
        UUID id = UUID.randomUUID();
        ToolCategoryRequestDTO dto = new ToolCategoryRequestDTO();
        dto.setToolCategoryName("Novo");

        ToolCategory mockCategory = ToolCategory.create("Novo");
        when(toolCategoryUseCase.updateToolCategory(id, "Novo")).thenReturn(mockCategory);
        when(toolCategoryPresenter.toToolCategoryResponseDTO(mockCategory))
                .thenReturn(ToolCategoryResponseDTO.builder().build());

        ToolCategoryResponseDTO result = toolCategoryController.updateToolCategory(id, dto);

        assertNotNull(result);
        verify(toolCategoryUseCase).updateToolCategory(id, "Novo");
    }

    @Test
    void reactivateToolCategory() {
        UUID id = UUID.randomUUID();
        ToolCategory mockCategory = ToolCategory.create("Cat");
        when(toolCategoryUseCase.reactivateToolCategory(id)).thenReturn(mockCategory);
        when(toolCategoryPresenter.toToolCategoryResponseDTO(mockCategory))
                .thenReturn(ToolCategoryResponseDTO.builder().build());

        ToolCategoryResponseDTO result = toolCategoryController.reactivateToolCategory(id);

        assertNotNull(result);
        verify(toolCategoryUseCase).reactivateToolCategory(id);
    }

    @Test
    void logicallyDeleteToolCategory() {
        UUID id = UUID.randomUUID();
        toolCategoryController.logicallyDeleteToolCategory(id);
        verify(toolCategoryUseCase).logicallyDeleteToolCategory(id);
    }
}
