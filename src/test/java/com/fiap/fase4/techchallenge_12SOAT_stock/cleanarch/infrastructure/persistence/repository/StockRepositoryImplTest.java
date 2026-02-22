package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.repository;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.Stock;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.entity.StockEntity;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.mapper.StockMapper;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.repository.jpa.StockJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockRepositoryImplTest {

        @Mock
        private StockJpaRepository stockJpaRepository;

        @Mock
        private StockMapper stockMapper;

        @InjectMocks
        private StockRepositoryImpl stockRepository;

        @Test
        void save() {
                Stock stock = Stock.builder().build();
                StockEntity entity = new StockEntity();
                StockEntity savedEntity = new StockEntity();

                when(stockMapper.toEntity(stock)).thenReturn(entity);
                when(stockJpaRepository.save(entity)).thenReturn(savedEntity);
                when(stockMapper.toDomain(savedEntity)).thenReturn(stock);

                Stock result = stockRepository.save(stock);

                assertEquals(stock, result);
                verify(stockJpaRepository).save(entity);
        }

        @Test
        void findById() {
                UUID id = UUID.randomUUID();
                StockEntity entity = new StockEntity();
                Stock stock = Stock.builder().build();

                when(stockJpaRepository.findById(id)).thenReturn(Optional.of(entity));
                when(stockMapper.toDomain(entity)).thenReturn(stock);

                Optional<Stock> result = stockRepository.findById(id);

                assertTrue(result.isPresent());
                assertEquals(stock, result.get());
        }

        @Test
        void findActiveById() {
                UUID id = UUID.randomUUID();
                StockEntity entity = new StockEntity();
                Stock stock = Stock.builder().build();

                when(stockJpaRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(entity));
                when(stockMapper.toDomain(entity)).thenReturn(stock);

                Optional<Stock> result = stockRepository.findActiveById(id);

                assertTrue(result.isPresent());
                assertEquals(stock, result.get());
        }

        @Test
        void findByName() {
                String name = "Name";
                StockEntity entity = new StockEntity();
                Stock stock = Stock.builder().build();

                when(stockJpaRepository.findByToolName(name)).thenReturn(Optional.of(entity));
                when(stockMapper.toDomain(entity)).thenReturn(stock);

                Optional<Stock> result = stockRepository.findByName(name);

                assertTrue(result.isPresent());
        }

        @Test
        void findAllActive() {
                StockEntity entity = new StockEntity();
                Stock stock = Stock.builder().build();

                when(stockJpaRepository.findByActiveTrue()).thenReturn(List.of(entity));
                when(stockMapper.toDomain(entity)).thenReturn(stock);

                List<Stock> result = stockRepository.findAllActive();

                assertEquals(1, result.size());
        }

        @Test
        void findAll() {
                StockEntity entity = new StockEntity();
                Stock stock = Stock.builder().build();

                when(stockJpaRepository.findAll()).thenReturn(List.of(entity));
                when(stockMapper.toDomain(entity)).thenReturn(stock);

                List<Stock> result = stockRepository.findAll();

                assertEquals(1, result.size());
        }
}
