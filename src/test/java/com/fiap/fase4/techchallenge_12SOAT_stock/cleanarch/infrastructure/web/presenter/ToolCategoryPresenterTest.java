package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ToolCategoryPresenterTest {

    @Test
    void toToolCategoryResponseDTO_ShouldConvertCorrectly() {
        UUID id = UUID.randomUUID();
        String name = "Wrenches";
        ToolCategory toolCategory = new ToolCategory(id, name, true);

        ToolCategoryPresenter presenter = new ToolCategoryPresenter();
        ToolCategoryResponseDTO dto = presenter.toToolCategoryResponseDTO(toolCategory);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(name, dto.getToolCategoryName());
        assertTrue(dto.getActive());
    }

}
