package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.useCases;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.exception.NotFoundException;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.gateway.ToolCategoryGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolCategoryUseCaseTest {

    @Mock
    private ToolCategoryGateway toolCategoryGateway;

    @InjectMocks
    private ToolCategoryUseCase toolCategoryUseCase;

    @Test
    void createToolCategory_shouldCreateAndSave() {
        // Arrange
        String name = "Elétricas";
        ToolCategory savedCategory = ToolCategory.create(name);
        when(toolCategoryGateway.save(any(ToolCategory.class))).thenReturn(savedCategory);

        // Act
        ToolCategory result = toolCategoryUseCase.createToolCategory(name);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getToolCategoryName());
        verify(toolCategoryGateway).save(any(ToolCategory.class));
    }

    @Test
    void updateToolCategory_shouldUpdateExistingCategory() {
        // Arrange
        UUID id = UUID.randomUUID();
        String oldName = "Velha";
        String newName = "Nova";
        ToolCategory existing = ToolCategory.create(oldName);

        when(toolCategoryGateway.findById(id)).thenReturn(Optional.of(existing));
        when(toolCategoryGateway.findByName(newName)).thenReturn(Optional.empty());
        when(toolCategoryGateway.save(any(ToolCategory.class))).thenReturn(existing);

        // Act
        ToolCategory result = toolCategoryUseCase.updateToolCategory(id, newName);

        // Assert
        assertEquals(newName, result.getToolCategoryName());
        verify(toolCategoryGateway).save(existing);
    }

    @Test
    void updateToolCategory_notFoundEx() {
        when(toolCategoryGateway.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> toolCategoryUseCase.updateToolCategory(UUID.randomUUID(), "Name"));
    }

    @Test
    void updateToolCategory_nameAlreadyInUse() {
        UUID id = UUID.randomUUID();
        ToolCategory existing = new ToolCategory(id, "Velha", true);
        ToolCategory another = new ToolCategory(UUID.randomUUID(), "Nova", true);

        // Simular que 'another' tem ID diferente

        when(toolCategoryGateway.findById(id)).thenReturn(Optional.of(existing));
        when(toolCategoryGateway.findByName("Nova")).thenReturn(Optional.of(another)); // outro item usa esse nome

        assertThrows(IllegalArgumentException.class, () -> toolCategoryUseCase.updateToolCategory(id, "Nova"));
    }

    @Test
    void getToolCategoryById_found() {
        UUID id = UUID.randomUUID();
        ToolCategory category = ToolCategory.create("Elétricas");
        when(toolCategoryGateway.findById(id)).thenReturn(Optional.of(category));

        ToolCategory result = toolCategoryUseCase.getToolCategoryById(id);

        assertNotNull(result);
        assertEquals("Elétricas", result.getToolCategoryName());
    }

    @Test
    void getToolCategoryById_notFound() {
        when(toolCategoryGateway.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> toolCategoryUseCase.getToolCategoryById(UUID.randomUUID()));
    }

    @Test
    void getAllToolCategories() {
        when(toolCategoryGateway.findAll()).thenReturn(List.of(ToolCategory.create("A"), ToolCategory.create("B")));

        List<ToolCategory> result = toolCategoryUseCase.getAllToolCategories();

        assertEquals(2, result.size());
    }

    @Test
    void getAllToolCategoriesActive() {
        when(toolCategoryGateway.findAllActive()).thenReturn(List.of(ToolCategory.create("A")));

        List<ToolCategory> result = toolCategoryUseCase.getAllToolCategoriesActive();

        assertEquals(1, result.size());
    }

    @Test
    void logicallyDeleteToolCategory() {
        UUID id = UUID.randomUUID();
        ToolCategory category = ToolCategory.create("A");
        when(toolCategoryGateway.findById(id)).thenReturn(Optional.of(category));

        toolCategoryUseCase.logicallyDeleteToolCategory(id);

        verify(toolCategoryGateway).save(category);
        assertFalse(category.getActive());
    }

    @Test
    void reactivateToolCategory() {
        UUID id = UUID.randomUUID();
        ToolCategory category = ToolCategory.create("A");
        category.deactivate();
        when(toolCategoryGateway.findById(id)).thenReturn(Optional.of(category));
        when(toolCategoryGateway.save(category)).thenReturn(category);

        ToolCategory result = toolCategoryUseCase.reactivateToolCategory(id);

        assertTrue(result.getActive());
        verify(toolCategoryGateway).save(category);
    }
}
