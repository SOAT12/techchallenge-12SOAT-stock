package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.mapper;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.entity.ToolCategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class ToolCategoryMapper {

    public ToolCategory toDomain(ToolCategoryEntity entity) {
        if (entity == null) return null;
        return new ToolCategory(
                entity.getId(),
                entity.getToolCategoryName(),
                entity.getActive());
    }

    public ToolCategoryEntity toEntity(ToolCategory domain) {
        if (domain == null) return null;
        return ToolCategoryEntity.builder()
                .id(domain.getId())
                .toolCategoryName(domain.getToolCategoryName())
                .active(domain.getActive())
                .build();
    }
}
