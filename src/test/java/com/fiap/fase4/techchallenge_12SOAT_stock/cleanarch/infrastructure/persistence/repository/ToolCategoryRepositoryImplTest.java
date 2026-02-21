package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.repository;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.entity.ToolCategoryEntity;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.mapper.ToolCategoryMapper;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.repository.jpa.ToolCategoryJpaRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ToolCategoryRepositoryImplTest {

    @Mock
    private ToolCategoryJpaRepository toolCategoryJpaRepository;

    @Mock
    private ToolCategoryMapper toolCategoryMapper;

    @InjectMocks
    private ToolCategoryRepositoryImpl toolCategoryRepository;

    private UUID id;
    private ToolCategory domain;
    private ToolCategoryEntity entity;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        domain = mock(ToolCategory.class);
        entity = mock(ToolCategoryEntity.class);
    }

    @Test
    void shouldReturnDomain_whenFindByIdExists() {
        when(toolCategoryJpaRepository.findById(id))
                .thenReturn(Optional.of(entity));
        when(toolCategoryMapper.toDomain(entity))
                .thenReturn(domain);

        Optional<ToolCategory> result = toolCategoryRepository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());

        verify(toolCategoryMapper).toDomain(entity);
    }

    @Test
    void shouldReturnEmpty_whenFindByIdDoesNotExist() {
        when(toolCategoryJpaRepository.findById(id))
                .thenReturn(Optional.empty());

        Optional<ToolCategory> result = toolCategoryRepository.findById(id);

        assertTrue(result.isEmpty());
        verify(toolCategoryMapper, never()).toDomain(any());
    }

    @Test
    void shouldReturnDomain_whenFindByToolCategoryNameExists() {
        when(toolCategoryJpaRepository.findByToolCategoryName("MANUAL"))
                .thenReturn(Optional.of(entity));
        when(toolCategoryMapper.toDomain(entity))
                .thenReturn(domain);

        Optional<ToolCategory> result =
                toolCategoryRepository.findByToolCategoryName("MANUAL");

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void shouldReturnMappedList_whenFindAllActive() {
        when(toolCategoryJpaRepository.findByActiveTrue())
                .thenReturn(List.of(entity));
        when(toolCategoryMapper.toDomain(entity))
                .thenReturn(domain);

        List<ToolCategory> result = toolCategoryRepository.findAllActive();

        assertEquals(1, result.size());
        assertEquals(domain, result.getFirst());

        verify(toolCategoryMapper).toDomain(entity);
    }

    @Test
    void shouldReturnMappedList_whenFindAll() {
        when(toolCategoryJpaRepository.findAll())
                .thenReturn(List.of(entity));
        when(toolCategoryMapper.toDomain(entity))
                .thenReturn(domain);

        List<ToolCategory> result = toolCategoryRepository.findAll();

        assertEquals(1, result.size());
        assertEquals(domain, result.getFirst());
    }

    @Test
    void shouldSaveToolCategory() {
        when(toolCategoryMapper.toEntity(domain))
                .thenReturn(entity);
        when(toolCategoryJpaRepository.save(entity))
                .thenReturn(entity);
        when(toolCategoryMapper.toDomain(entity))
                .thenReturn(domain);

        ToolCategory result = toolCategoryRepository.save(domain);

        assertNotNull(result);
        assertEquals(domain, result);

        verify(toolCategoryMapper).toEntity(domain);
        verify(toolCategoryJpaRepository).save(entity);
        verify(toolCategoryMapper).toDomain(entity);
    }

}
