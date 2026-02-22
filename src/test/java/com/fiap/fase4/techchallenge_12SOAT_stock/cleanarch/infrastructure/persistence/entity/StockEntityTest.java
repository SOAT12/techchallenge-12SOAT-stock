package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StockEntityTest {

    @Test
    void testStockEntityBuilderAndGetters() {
        // Arrange
        UUID id = UUID.randomUUID();
        String toolName = "Furadeira";
        BigDecimal value = new BigDecimal("150.00");
        int quantity = 10;
        ToolCategoryEntity category = new ToolCategoryEntity();
        category.setId(UUID.randomUUID());

        // Act
        StockEntity entity = StockEntity.builder()
                .id(id)
                .toolName(toolName)
                .value(value)
                .active(true)
                .quantity(quantity)
                .toolCategoryEntity(category)
                .build();

        // Assert
        assertEquals(id, entity.getId());
        assertEquals(toolName, entity.getToolName());
        assertEquals(value, entity.getValue());
        assertTrue(entity.getActive());
        assertEquals(quantity, entity.getQuantity());
        assertEquals(category, entity.getToolCategoryEntity());
    }

    @Test
    void testStockEntitySetters() {
        // Arrange
        StockEntity entity = new StockEntity();
        UUID id = UUID.randomUUID();
        String toolName = "Martelo";
        BigDecimal value = new BigDecimal("50.00");
        int quantity = 5;
        ToolCategoryEntity category = new ToolCategoryEntity();

        // Act
        entity.setId(id);
        entity.setToolName(toolName);
        entity.setValue(value);
        entity.setActive(false);
        entity.setQuantity(quantity);
        entity.setToolCategoryEntity(category);

        // Assert
        assertEquals(id, entity.getId());
        assertEquals(toolName, entity.getToolName());
        assertEquals(value, entity.getValue());
        assertFalse(entity.getActive());
        assertEquals(quantity, entity.getQuantity());
        assertEquals(category, entity.getToolCategoryEntity());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UUID id = UUID.randomUUID();
        StockEntity entity1 = StockEntity.builder().id(id).toolName("A").build();
        StockEntity entity2 = StockEntity.builder().id(id).toolName("A").build();
        StockEntity entity3 = StockEntity.builder().id(UUID.randomUUID()).toolName("B").build();

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
