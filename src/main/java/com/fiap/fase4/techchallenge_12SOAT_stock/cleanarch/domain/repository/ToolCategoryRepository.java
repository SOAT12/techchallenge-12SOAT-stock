package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.repository;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ToolCategoryRepository {

    Optional<ToolCategory> findById(UUID toolCategoryId);
    Optional<ToolCategory> findByToolCategoryName(String toolCategoryName);
    List<ToolCategory> findAllActive();
    List<ToolCategory> findAll();
    ToolCategory save(ToolCategory newToolCategory);
}
