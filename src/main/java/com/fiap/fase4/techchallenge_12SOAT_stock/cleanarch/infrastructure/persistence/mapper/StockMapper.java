package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.mapper;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.Stock;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.entity.StockEntity;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.entity.ToolCategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {

    public Stock toDomain(StockEntity entity) {
        if (entity == null) return null;

        ToolCategory toolCategory = new ToolCategory(
                entity.getToolCategoryEntity().getId(),
                entity.getToolCategoryEntity().getToolCategoryName(),
                entity.getToolCategoryEntity().getActive());

        return new Stock(
                entity.getId(),
                entity.getToolName(),
                entity.getValue(),
                entity.getQuantity(),
                toolCategory,
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public StockEntity toEntity(Stock domain) {
        if (domain == null) return null;

        ToolCategoryEntity categoryEntity = ToolCategoryEntity.builder()
                .id(domain.getToolCategory().getId())
                .toolCategoryName(domain.getToolCategory().getToolCategoryName())
                .active(domain.getToolCategory().getActive())
                .build();

        return StockEntity.builder()
                .id(domain.getId())
                .toolName(domain.getToolName())
                .value(domain.getValue())
                .quantity(domain.getQuantity())
                .active(domain.isActive())
                .toolCategoryEntity(categoryEntity)
                .build();
    }
}
