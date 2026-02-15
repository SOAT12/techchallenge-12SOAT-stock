package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.gateway;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.Stock;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StockGateway {

    private final StockRepository stockRepository;

    public Optional<Stock> findByName(String toolName) {
        return stockRepository.findByName(toolName);
    }

    public Stock save(Stock newStock) {
        return stockRepository.save(newStock);
    }

    public Optional<Stock> findActiveById(UUID id) {
        return stockRepository.findActiveById(id);
    }

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public List<Stock> findAllActive() {
        return stockRepository.findAllActive();
    }

    public Optional<Stock> findById(UUID id) {
        return stockRepository.findById(id);
    }
}
