package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.repository;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.Stock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockRepository {

    Stock save(Stock stock);
    Optional<Stock> findById(UUID stockItemId);
    List<Stock> findAllActive();
    Optional<Stock> findActiveById(UUID stockItemId);
    Optional<Stock> findByName(String name);
    List<Stock> findAll();
}
