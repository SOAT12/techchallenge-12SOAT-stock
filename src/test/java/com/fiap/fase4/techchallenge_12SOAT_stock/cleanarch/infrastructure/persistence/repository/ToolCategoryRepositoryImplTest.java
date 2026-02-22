package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.repository;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.entity.ToolCategoryEntity;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.mapper.ToolCategoryMapper;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.repository.jpa.ToolCategoryJpaRepository;
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
class ToolCategoryRepositoryImplTest {

        @Mock
        private ToolCategoryJpaRepository toolCategoryJpaRepository;

        @Mock
        private ToolCategoryMapper toolCategoryMapper;

        @InjectMocks
        private ToolCategoryRepositoryImpl toolCategoryRepository;

        @Test
        void findById() {
                UUID id = UUID.randomUUID();
                ToolCategoryEntity entity = new ToolCategoryEntity();
                ToolCategory domain = ToolCategory.create("A");

                when(toolCategoryJpaRepository.findById(id)).thenReturn(Optional.of(entity));
                when(toolCategoryMapper.toDomain(entity)).thenReturn(domain);

                Optional<ToolCategory> result = toolCategoryRepository.findById(id);

                assertTrue(result.isPresent());
                assertEquals(domain, result.get());
        }

        @Test
        void findByToolCategoryName() {
                String name = "A";
                ToolCategoryEntity entity = new ToolCategoryEntity();
                ToolCategory domain = ToolCategory.create("A");

                when(toolCategoryJpaRepository.findByToolCategoryName(name)).thenReturn(Optional.of(entity));
                when(toolCategoryMapper.toDomain(entity)).thenReturn(domain);

                Optional<ToolCategory> result = toolCategoryRepository.findByToolCategoryName(name);

                assertTrue(result.isPresent());
                assertEquals(domain, result.get());
        }

        @Test
        void findAllActive() {
                ToolCategoryEntity entity = new ToolCategoryEntity();
                ToolCategory domain = ToolCategory.create("A");

                when(toolCategoryJpaRepository.findByActiveTrue()).thenReturn(List.of(entity));
                when(toolCategoryMapper.toDomain(entity)).thenReturn(domain);

                List<ToolCategory> result = toolCategoryRepository.findAllActive();

                assertEquals(1, result.size());
        }

        @Test
        void findAll() {
                ToolCategoryEntity entity = new ToolCategoryEntity();
                ToolCategory domain = ToolCategory.create("A");

                when(toolCategoryJpaRepository.findAll()).thenReturn(List.of(entity));
                when(toolCategoryMapper.toDomain(entity)).thenReturn(domain);

                List<ToolCategory> result = toolCategoryRepository.findAll();

                assertEquals(1, result.size());
        }

        @Test
        void save() {
                ToolCategory domain = ToolCategory.create("A");
                ToolCategoryEntity entity = new ToolCategoryEntity();
                ToolCategoryEntity saved = new ToolCategoryEntity();

                when(toolCategoryMapper.toEntity(domain)).thenReturn(entity);
                when(toolCategoryJpaRepository.save(entity)).thenReturn(saved);
                when(toolCategoryMapper.toDomain(saved)).thenReturn(domain);

                ToolCategory result = toolCategoryRepository.save(domain);

                assertEquals(domain, result);
                verify(toolCategoryJpaRepository).save(entity);
        }
}
