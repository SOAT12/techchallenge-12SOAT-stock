package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ToolCategoryTest {

    @Test
    void testCreateValidCategory() {
        ToolCategory category = ToolCategory.create("Elétricas");

        assertNotNull(category);
        assertEquals("Elétricas", category.getToolCategoryName());
        assertTrue(category.getActive());
        assertNull(category.getId());
    }

    @Test
    void testCreateInvalidCategory() {
        assertThrows(IllegalArgumentException.class, () -> ToolCategory.create("    "));
        assertThrows(IllegalArgumentException.class, () -> ToolCategory.create(null));
    }

    @Test
    void testChangeName() {
        ToolCategory category = ToolCategory.create("Elétricas");
        category.changeName("Manuais");
        assertEquals("Manuais", category.getToolCategoryName());

        assertThrows(IllegalArgumentException.class, () -> category.changeName(null));
    }

    @Test
    void testActivateAndDeactivate() {
        ToolCategory category = ToolCategory.create("Elétricas");

        category.deactivate();
        assertFalse(category.getActive());

        assertThrows(IllegalStateException.class, category::deactivate);

        category.activate();
        assertTrue(category.getActive());

        assertThrows(IllegalArgumentException.class, category::activate);
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        ToolCategory cat1 = new ToolCategory(id, "A", true);
        ToolCategory cat2 = new ToolCategory(id, "B", true);
        ToolCategory cat3 = new ToolCategory(UUID.randomUUID(), "C", true);

        assertEquals(cat1, cat1);
        assertEquals(cat1, cat2);
        assertNotEquals(cat1, cat3);
        assertNotEquals(null, cat1);
        assertNotEquals(new Object(), cat1);

        assertEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        ToolCategory cat1 = new ToolCategory(id, "A", true);
        String s = cat1.toString();
        assertTrue(s.contains("id=" + id));
        assertTrue(s.contains("toolCategoryName='A'"));
    }
}
