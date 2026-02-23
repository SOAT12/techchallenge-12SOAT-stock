package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    void testCreateValidStock() {
        // Arrange
        ToolCategory category = ToolCategory.create("Ferramentas");

        // Act
        Stock stock = Stock.create("Martelo", new BigDecimal("50.0"), 10, category);

        // Assert
        assertNotNull(stock);
        assertEquals("Martelo", stock.getToolName());
        assertEquals(new BigDecimal("50.0"), stock.getValue());
        assertEquals(10, stock.getQuantity());
        assertEquals(category, stock.getToolCategory());
        assertTrue(stock.isActive());
        assertNotNull(stock.getCreatedAt());
        assertNotNull(stock.getUpdatedAt());
        assertNull(stock.getId()); // Not set by create method
    }

    @Test
    void testCreateInvalidStockThrowsException() {
        // Arrange
        ToolCategory category = ToolCategory.create("Ferramentas");

        // Act & Assert
        Exception e1 = assertThrows(IllegalArgumentException.class,
                () -> Stock.create("", new BigDecimal("10.0"), 5, category));
        assertEquals("Nome do item não pode ser nulo ou vazio.", e1.getMessage());

        Exception e2 = assertThrows(IllegalArgumentException.class,
                () -> Stock.create("Martelo", new BigDecimal("-1.0"), 5, category));
        assertEquals("Valor não pode ser nulo ou menor que zero.", e2.getMessage());

        Exception e3 = assertThrows(IllegalArgumentException.class,
                () -> Stock.create("Martelo", new BigDecimal("10.0"), -1, category));
        assertEquals("Quantidade não pode ser nula ou menor que zero.", e3.getMessage());

        Exception e4 = assertThrows(IllegalArgumentException.class,
                () -> Stock.create("Martelo", new BigDecimal("10.0"), 5, null));
        assertEquals("Uma categoria válida é necessária.", e4.getMessage());
    }

    @Test
    void testAddStock() {
        // Arrange
        Stock stock = Stock.create("Martelo", new BigDecimal("50.0"), 10, ToolCategory.create("Ferramentas"));

        // Act
        stock.addStock(5);

        // Assert
        assertEquals(15, stock.getQuantity());
        assertTrue(stock.isActive());
    }

    @Test
    void testAddStockInvalidQuantity() {
        Stock stock = Stock.create("Martelo", new BigDecimal("50.0"), 10, ToolCategory.create("Ferramentas"));

        Exception e = assertThrows(IllegalArgumentException.class, () -> stock.addStock(-1));
        assertEquals("Quantidade não pode ser nula ou menor do que zero.", e.getMessage());
    }

    @Test
    void testRemoveStock() {
        // Arrange
        Stock stock = Stock.create("Martelo", new BigDecimal("50.0"), 10, ToolCategory.create("Ferramentas"));

        // Act
        stock.removeStock(3);

        // Assert
        assertEquals(7, stock.getQuantity());
    }

    @Test
    void testRemoveStockExceptions() {
        Stock stock = Stock.create("Martelo", new BigDecimal("50.0"), 10, ToolCategory.create("Ferramentas"));

        Exception e1 = assertThrows(IllegalArgumentException.class, () -> stock.removeStock(0));
        assertEquals("Quantidade não pode ser nula ou menor do que zero.", e1.getMessage());

        Exception e2 = assertThrows(IllegalArgumentException.class, () -> stock.removeStock(11));
        assertEquals("Quantidade em estoque menor do que a informada.", e2.getMessage());
    }

    @Test
    void testDeactivateAndActivate() {
        Stock stock = Stock.create("Martelo", new BigDecimal("50.0"), 10, ToolCategory.create("Ferramentas"));

        // Act - Deactivate
        stock.deactivate();
        assertFalse(stock.isActive());

        // Act - Deactivate again throws exception
        assertThrows(IllegalStateException.class, stock::deactivate);

        // Act - Activate
        stock.activate();
        assertTrue(stock.isActive());

        // Act - Activate again throws exception
        assertThrows(IllegalArgumentException.class, stock::activate);
    }

    @Test
    void testUpdateDetails() {
        Stock stock = Stock.create("Martelo", new BigDecimal("50.0"), 10, ToolCategory.create("Ferramentas"));
        ToolCategory newCategory = ToolCategory.create("Novas Ferramentas");

        stock.updateDetails("Marreta", new BigDecimal("100.0"), false, newCategory);

        assertEquals("Marreta", stock.getToolName());
        assertEquals(new BigDecimal("100.0"), stock.getValue());
        assertFalse(stock.isActive());
        assertEquals(newCategory, stock.getToolCategory());
    }

    @Test
    void testUpdateDetailsExceptions() {
        Stock stock = Stock.create("Martelo", new BigDecimal("50.0"), 10, ToolCategory.create("Ferramentas"));
        ToolCategory category = ToolCategory.create("Categoria");

        assertThrows(IllegalArgumentException.class,
                () -> stock.updateDetails(null, new BigDecimal("10.0"), true, category));
        assertThrows(IllegalArgumentException.class,
                () -> stock.updateDetails("Nome", new BigDecimal("-1.0"), true, category));
        assertThrows(IllegalArgumentException.class,
                () -> stock.updateDetails("Nome", new BigDecimal("10.0"), true, null));
    }

    @Test
    void testEqualsAndHashCodeAndBuilder() {
        UUID id = UUID.randomUUID();
        Stock stock1 = Stock.builder().id(id).toolName("A").build();
        Stock stock2 = Stock.builder().id(id).toolName("B").build();
        Stock stock3 = Stock.builder().id(UUID.randomUUID()).build();

        assertEquals(stock1, stock1);
        assertEquals(stock1, stock2);
        assertNotEquals(stock1, stock3);
        assertNotEquals(null, stock1);
        assertNotEquals(new Object(), stock1);
        assertEquals(stock1.hashCode(), stock2.hashCode());

        stock1.setQuantity(50);
        assertEquals(50, stock1.getQuantity());
    }
}
