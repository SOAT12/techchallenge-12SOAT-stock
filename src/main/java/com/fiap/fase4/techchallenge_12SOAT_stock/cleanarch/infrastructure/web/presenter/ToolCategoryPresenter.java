package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ToolCategoryPresenter {

    public ToolCategoryResponseDTO toToolCategoryResponseDTO(ToolCategory toolCategory) {
        return ToolCategoryResponseDTO.builder()
                .id(toolCategory.getId())
                .toolCategoryName(toolCategory.getToolCategoryName())
                .active(toolCategory.getActive())
                .build();
    }
}
