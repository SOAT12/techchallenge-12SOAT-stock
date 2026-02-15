package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.controller;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.Stock;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.StockAvailabilityItem;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.StockAvailabilityResult;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.useCases.StockUseCase;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.StockPresenter;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class StockController {

    private final StockUseCase stockUseCase;
    private final StockPresenter stockPresenter;

    public StockController(StockUseCase stockUseCase, StockPresenter stockPresenter) {
        this.stockUseCase = stockUseCase;
        this.stockPresenter = stockPresenter;
    }

    public StockResponseDTO createStock(CreateStockRequestDTO dto) {
        Stock stock = stockUseCase.createStock(dto.getToolName(), dto.getValue(), dto.getQuantity(), dto.getToolCategoryId());
        return stockPresenter.toStockResponseDTO(stock);
    }

    public StockResponseDTO getStockById(UUID id) {
        Stock stock = stockUseCase.findStockItemById(id);
        return stockPresenter.toStockResponseDTO(stock);
    }

    public List<StockResponseDTO> getAllStockItems() {
        return stockUseCase.getAllStock().stream()
                .map(stockPresenter::toStockResponseDTO)
                .toList();
    }

    public List<StockResponseDTO> getAllStockItemsActive() {
        return stockUseCase.getAllActiveStockItems().stream()
                .map(stockPresenter::toStockResponseDTO)
                .toList();
    }

    public StockResponseDTO updateStock(UUID id, StockRequestDTO dto) {
        Stock stock = stockUseCase.updateStockItem(id, dto.getToolName(), dto.getValue(), dto.getQuantity(), dto.getActive(), dto.getToolCategoryId());
        return stockPresenter.toStockResponseDTO(stock);
    }

    public StockResponseDTO reactivateStock(UUID id) {
        Stock reactivated = stockUseCase.reactivateStockItem(id);
        return stockPresenter.toStockResponseDTO(reactivated);
    }

    public void logicallyDeleteStock(UUID id) {
        stockUseCase.inactivateStockItem(id);
    }

    public StockAvailabilityResponseDTO getStockAvailability(StockAvailabilityRequestDTO request) {
        List<StockAvailabilityItem> items = request.getItems().stream()
                .map(i -> new StockAvailabilityItem(i.getStockId(), i.getQuantity()))
                .collect(Collectors.toList());
        StockAvailabilityResult result = stockUseCase.getStockAvailability(items);
        return stockPresenter.toStockAvailabilityResponseDTO(result);
    }
}
