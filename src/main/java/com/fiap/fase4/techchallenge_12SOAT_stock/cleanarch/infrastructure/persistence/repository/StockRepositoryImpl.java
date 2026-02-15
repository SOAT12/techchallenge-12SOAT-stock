package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.repository;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.Stock;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.repository.StockRepository;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.entity.StockEntity;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.mapper.StockMapper;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.repository.jpa.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepository {

    private final StockJpaRepository stockJpaRepository;
    private final StockMapper stockMapper;

    @Override
    public Stock save(Stock stock) {
        StockEntity entity = stockMapper.toEntity(stock);
        StockEntity savedEntity = stockJpaRepository.save(entity);
        return stockMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Stock> findById(UUID stockItemId) {
        return stockJpaRepository.findById(stockItemId)
                .map(stockMapper::toDomain);
    }

    @Override
    public Optional<Stock> findActiveById(UUID stockItemId) {
        return stockJpaRepository.findByIdAndActiveTrue(stockItemId)
                .map(stockMapper::toDomain);
    }

    @Override
    public Optional<Stock> findByName(String name) {
        return stockJpaRepository.findByToolName(name)
                .map(stockMapper::toDomain);
    }

    @Override
    public List<Stock> findAllActive() {
        return stockJpaRepository.findByActiveTrue().stream()
                .map(stockMapper::toDomain)
                .toList();
    }

    @Override
    public List<Stock> findAll() {
        return stockJpaRepository.findAll().stream()
                .map(stockMapper::toDomain)
                .toList();
    }
}
