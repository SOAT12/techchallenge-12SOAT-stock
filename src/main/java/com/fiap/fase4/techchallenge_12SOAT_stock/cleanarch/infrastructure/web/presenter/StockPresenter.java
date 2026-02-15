package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.Stock;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.StockAvailabilityResult;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.StockAvailabilityResponseDTO;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.StockResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockPresenter {

    private final ToolCategoryPresenter toolCategoryPresenter;

    public StockPresenter(ToolCategoryPresenter toolCategoryPresenter) {
        this.toolCategoryPresenter = toolCategoryPresenter;
    }

    public StockResponseDTO toStockResponseDTO(Stock stock) {
        return StockResponseDTO.builder()
                .id(stock.getId())
                .toolName(stock.getToolName())
                .value(stock.getValue())
                .isActive(stock.isActive())
                .quantity(stock.getQuantity())
                .created_at(stock.getCreatedAt())
                .updated_at(stock.getUpdatedAt())
                .toolCategory(toolCategoryPresenter.toToolCategoryResponseDTO(stock.getToolCategory()))
                .build();
    }

    public StockAvailabilityResponseDTO toStockAvailabilityResponseDTO(StockAvailabilityResult result) {
        List<StockAvailabilityResponseDTO.MissingItemDTO> missing = result.missingItems().stream()
                .map(m -> new StockAvailabilityResponseDTO.MissingItemDTO(
                        m.stockId(),
                        m.name(),
                        m.requiredQuantity(),
                        m.availableQuantity()
                ))
                .collect(Collectors.toList());
        return new StockAvailabilityResponseDTO(result.allAvailable(), missing);
    }
}
