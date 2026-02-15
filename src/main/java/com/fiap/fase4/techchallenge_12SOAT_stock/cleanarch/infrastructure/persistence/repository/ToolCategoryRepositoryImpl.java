package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.repository;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.repository.ToolCategoryRepository;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.entity.ToolCategoryEntity;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.mapper.ToolCategoryMapper;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.persistence.repository.jpa.ToolCategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ToolCategoryRepositoryImpl implements ToolCategoryRepository {

    private final ToolCategoryJpaRepository toolCategoryJpaRepository;
    private final ToolCategoryMapper toolCategoryMapper;

    @Override
    public Optional<ToolCategory> findById(UUID toolCategoryId) {
        return toolCategoryJpaRepository.findById(toolCategoryId)
                .map(toolCategoryMapper::toDomain);
    }

    @Override
    public Optional<ToolCategory> findByToolCategoryName(String toolCategoryName) {
        return toolCategoryJpaRepository.findByToolCategoryName(toolCategoryName)
                .map(toolCategoryMapper::toDomain);
    }

    @Override
    public List<ToolCategory> findAllActive() {
        return toolCategoryJpaRepository.findByActiveTrue().stream()
                .map(toolCategoryMapper::toDomain)
                .toList();
    }

    @Override
    public List<ToolCategory> findAll() {
        return toolCategoryJpaRepository.findAll().stream()
                .map(toolCategoryMapper::toDomain)
                .toList();
    }

    @Override
    public ToolCategory save(ToolCategory newToolCategory) {
        ToolCategoryEntity entity = toolCategoryMapper.toEntity(newToolCategory);
        ToolCategoryEntity saved = toolCategoryJpaRepository.save(entity);
        return toolCategoryMapper.toDomain(saved);
    }
}
