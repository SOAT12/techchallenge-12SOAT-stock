package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.repository.jpa;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockJpaRepository extends JpaRepository<StockEntity, UUID> {

    Optional<StockEntity> findByIdAndActiveTrue(UUID id);
    List<StockEntity> findByActiveTrue();
    Optional<StockEntity> findByToolName(String name);
}
