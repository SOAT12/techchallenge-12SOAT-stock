package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.repository;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.Stock;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.entity.StockEntity;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.mapper.StockMapper;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.repository.jpa.StockJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockRepositoryImplTest {

    @Mock
    private StockJpaRepository stockJpaRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockRepositoryImpl stockRepository;

    private UUID id;
    private Stock stock;
    private StockEntity entity;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        stock = mock(Stock.class);
        entity = mock(StockEntity.class);
    }

    @Test
    void shouldSaveStock() {
        when(stockMapper.toEntity(stock)).thenReturn(entity);
        when(stockJpaRepository.save(entity)).thenReturn(entity);
        when(stockMapper.toDomain(entity)).thenReturn(stock);

        Stock result = stockRepository.save(stock);

        assertNotNull(result);
        assertEquals(stock, result);

        verify(stockMapper).toEntity(stock);
        verify(stockJpaRepository).save(entity);
        verify(stockMapper).toDomain(entity);
    }

    @Test
    void shouldReturnStock_whenFindByIdExists() {
        when(stockJpaRepository.findById(id))
                .thenReturn(Optional.of(entity));
        when(stockMapper.toDomain(entity))
                .thenReturn(stock);

        Optional<Stock> result = stockRepository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(stock, result.get());

        verify(stockMapper).toDomain(entity);
    }

    @Test
    void shouldReturnEmpty_whenFindByIdDoesNotExist() {
        when(stockJpaRepository.findById(id))
                .thenReturn(Optional.empty());

        Optional<Stock> result = stockRepository.findById(id);

        assertTrue(result.isEmpty());
        verify(stockMapper, never()).toDomain(any());
    }

    @Test
    void shouldReturnStock_whenFindActiveByIdExists() {
        when(stockJpaRepository.findByIdAndActiveTrue(id))
                .thenReturn(Optional.of(entity));
        when(stockMapper.toDomain(entity))
                .thenReturn(stock);

        Optional<Stock> result = stockRepository.findActiveById(id);

        assertTrue(result.isPresent());
        assertEquals(stock, result.get());
    }

    @Test
    void shouldReturnStock_whenFindByNameExists() {
        when(stockJpaRepository.findByToolName("Martelo"))
                .thenReturn(Optional.of(entity));
        when(stockMapper.toDomain(entity))
                .thenReturn(stock);

        Optional<Stock> result = stockRepository.findByName("Martelo");

        assertTrue(result.isPresent());
        assertEquals(stock, result.get());
    }

    @Test
    void shouldReturnMappedList_whenFindAllActive() {
        when(stockJpaRepository.findByActiveTrue())
                .thenReturn(List.of(entity));
        when(stockMapper.toDomain(entity))
                .thenReturn(stock);

        List<Stock> result = stockRepository.findAllActive();

        assertEquals(1, result.size());
        assertEquals(stock, result.get(0));
    }

    @Test
    void shouldReturnMappedList_whenFindAll() {
        when(stockJpaRepository.findAll())
                .thenReturn(List.of(entity));
        when(stockMapper.toDomain(entity))
                .thenReturn(stock);

        List<Stock> result = stockRepository.findAll();

        assertEquals(1, result.size());
        assertEquals(stock, result.get(0));
    }
}
