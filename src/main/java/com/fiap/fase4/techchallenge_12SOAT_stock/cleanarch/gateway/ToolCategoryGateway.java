package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.gateway;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.repository.ToolCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ToolCategoryGateway {

    private final ToolCategoryRepository toolCategoryRepository;

    public Optional<ToolCategory> findById(UUID toolCategoryId) {
        return toolCategoryRepository.findById(toolCategoryId);
    }

    public ToolCategory save(ToolCategory newToolCategory) {
        return toolCategoryRepository.save(newToolCategory);
    }

    public Optional<ToolCategory> findByName(String toolCategoryName) {
        return toolCategoryRepository.findByToolCategoryName(toolCategoryName);
    }

    public List<ToolCategory> findAll() {
        return toolCategoryRepository.findAll();
    }

    public List<ToolCategory> findAllActive() {
        return toolCategoryRepository.findAllActive();
    }
}
