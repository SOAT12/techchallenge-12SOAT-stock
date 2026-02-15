package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO {
    private UUID id;
    private String toolName;
    private BigDecimal value;
    private Boolean isActive;
    private Integer quantity;
    private Date created_at;
    private Date updated_at;
    private ToolCategoryResponseDTO toolCategory;
}
