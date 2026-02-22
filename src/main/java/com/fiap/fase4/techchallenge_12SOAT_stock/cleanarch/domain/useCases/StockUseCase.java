package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.useCases;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.Stock;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.StockAvailabilityItem;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.StockAvailabilityResult;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.exception.NotFoundException;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.gateway.StockGateway;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.gateway.ToolCategoryGateway;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.messaging.publisher.SqsEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StockUseCase {

    private static final String NOT_FOUND_STOCK_ITEM_MSG = "Item de estoque não encontrado.";
    private static final String WAITING_FOR_STOCK_OS_STATUS = "WAITING_FOR_STOCK";

    private final StockGateway stockGateway;
    private final ToolCategoryGateway toolCategoryGateway;
    private final SqsEventPublisher sqsEventPublisher;

    public Stock createStock(String toolName, BigDecimal value, Integer quantity, UUID toolCategoryId) {
        ToolCategory category = toolCategoryGateway.findById(toolCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

        stockGateway.findByName(toolName).ifPresent(existingItem -> {
            throw new IllegalArgumentException("Item já cadastrado.");
        });

        Stock newStock = Stock.create(toolName, value, quantity, category);
        return stockGateway.save(newStock);
    }

    public Stock updateStockItem(UUID id, String toolName, BigDecimal value, Integer quantity, Boolean isActive, UUID toolCategoryId) {
        Stock existingItem = stockGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STOCK_ITEM_MSG));

        if (!existingItem.getToolName().equalsIgnoreCase(toolName)) {
            stockGateway.findByName(toolName).ifPresent(item -> {
                if (!item.getId().equals(id)) {
                    throw new IllegalArgumentException(String.format("O nome da ferramenta %s já está em uso.", toolName));
                }
            });
        }

        ToolCategory category = toolCategoryGateway.findById(toolCategoryId)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        existingItem.updateDetails(toolName, value, isActive, category);

        int quantityDifference = quantity - existingItem.getQuantity();

        if (quantityDifference > 0) {
            existingItem.addStock(quantityDifference);
        } else if (quantityDifference < 0) {
            existingItem.removeStock(Math.abs(quantityDifference));
        }

        return stockGateway.save(existingItem);
    }

    public void addStock(UUID id, Integer quantity) {
        Stock existingItem = stockGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STOCK_ITEM_MSG));

        existingItem.addStock(quantity);
        stockGateway.save(existingItem);
    }

    public void removeStock(Long osId, UUID id, Integer quantity) {
        Stock existingItem = stockGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STOCK_ITEM_MSG));

        try {
            existingItem.removeStock(quantity);
        } catch (IllegalArgumentException e) {
            sqsEventPublisher.publishOsStatusUpdate(osId, WAITING_FOR_STOCK_OS_STATUS);
        }

        stockGateway.save(existingItem);
    }

    public List<Stock> getAllStock() {
        return stockGateway.findAll();
    }

    public List<Stock> getAllActiveStockItems() {
        return stockGateway.findAllActive();
    }

    public Stock findStockItemById(UUID id) {
        return stockGateway.findActiveById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STOCK_ITEM_MSG));
    }

    public void inactivateStockItem(UUID id) {
        Stock stock = stockGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STOCK_ITEM_MSG));
        Stock inactivatedStock = stock.deactivate();
        stockGateway.save(inactivatedStock);
    }

    public Stock reactivateStockItem(UUID id) {
        Stock stock = stockGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STOCK_ITEM_MSG));
        Stock activatedStock = stock.activate();
        return stockGateway.save(activatedStock);
    }

    public StockAvailabilityResult getStockAvailability(List<StockAvailabilityItem> items) {
        List<StockAvailabilityResult.MissingStockItem> missingItems = new ArrayList<>();
        for (StockAvailabilityItem item : items) {
            Stock availableStock = stockGateway.findActiveById(item.stockId()).orElse(null);
            if (availableStock == null) {
                missingItems.add(new StockAvailabilityResult.MissingStockItem(
                        item.stockId(),
                        "Item não encontrado",
                        item.quantity(),
                        0
                ));
                continue;
            }
            int requiredQuantity = item.quantity();
            int availableQuantity = availableStock.getQuantity();
            if (availableQuantity < requiredQuantity) {
                missingItems.add(new StockAvailabilityResult.MissingStockItem(
                        availableStock.getId(),
                        availableStock.getToolName(),
                        requiredQuantity,
                        availableQuantity
                ));
            }
        }
        boolean allItemsAvailable = missingItems.isEmpty();
        return new StockAvailabilityResult(allItemsAvailable, missingItems);
    }
}
