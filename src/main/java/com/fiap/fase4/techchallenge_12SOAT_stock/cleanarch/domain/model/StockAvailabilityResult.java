package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model;

import java.util.List;
import java.util.UUID;

/**
 * Resultado da consulta de disponibilidade de estoque (saída).
 */
public record StockAvailabilityResult(
    boolean allAvailable,
    List<MissingStockItem> missingItems
) {
    public record MissingStockItem(UUID stockId, String name, int requiredQuantity, int availableQuantity) {}
}
