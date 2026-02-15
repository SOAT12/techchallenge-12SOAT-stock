package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model;

import java.util.UUID;

/**
 * Item para consulta de disponibilidade de estoque (entrada).
 */
public record StockAvailabilityItem(UUID stockId, int quantity) {}
