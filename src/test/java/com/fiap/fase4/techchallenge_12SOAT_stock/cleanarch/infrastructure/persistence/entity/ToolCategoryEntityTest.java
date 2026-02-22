package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ToolCategoryEntityTest {

    @Test
    void testToolCategoryEntityBuilderAndGetters() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Ferramentas Manuais";

        // Act
        ToolCategoryEntity entity = ToolCategoryEntity.builder()
                .id(id)
                .toolCategoryName(name)
                .active(true)
                .build();

        // Assert
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getToolCategoryName());
        assertTrue(entity.getActive());
    }

    @Test
    void testToolCategoryEntitySetters() {
        // Arrange
        ToolCategoryEntity entity = new ToolCategoryEntity();
        UUID id = UUID.randomUUID();
        String name = "Elétricas";

        // Act
        entity.setId(id);
        entity.setToolCategoryName(name);
        entity.setActive(false);

        // Assert
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getToolCategoryName());
        assertFalse(entity.getActive());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UUID id = UUID.randomUUID();
        ToolCategoryEntity entity1 = ToolCategoryEntity.builder().id(id).toolCategoryName("A").build();
        ToolCategoryEntity entity2 = ToolCategoryEntity.builder().id(id).toolCategoryName("A").build();
        ToolCategoryEntity entity3 = ToolCategoryEntity.builder().id(UUID.randomUUID()).toolCategoryName("B").build();

        // Act & Assert
        assertEquals(entity1, entity1);
        assertEquals(entity1, entity2);
        assertNotEquals(entity1, entity3);
        assertNotEquals(null, entity1);
        assertNotEquals(new Object(), entity1);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }
}
