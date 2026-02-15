package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.controller;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.useCases.ToolCategoryUseCase;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.ToolCategoryPresenter;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.ToolCategoryRequestDTO;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ToolCategoryController {

    private final ToolCategoryUseCase toolCategoryUseCase;
    private final ToolCategoryPresenter toolCategoryPresenter;

    public ToolCategoryController(ToolCategoryUseCase toolCategoryUseCase, ToolCategoryPresenter toolCategoryPresenter) {
        this.toolCategoryUseCase = toolCategoryUseCase;
        this.toolCategoryPresenter = toolCategoryPresenter;
    }

    public ToolCategoryResponseDTO createToolCategory(ToolCategoryRequestDTO requestDTO) {
        ToolCategory toolCategory = toolCategoryUseCase.createToolCategory(requestDTO.getToolCategoryName());
        return toolCategoryPresenter.toToolCategoryResponseDTO(toolCategory);
    }

    public ToolCategoryResponseDTO getToolCategoryById(UUID id) {
        ToolCategory toolCategory = toolCategoryUseCase.getToolCategoryById(id);
        return toolCategoryPresenter.toToolCategoryResponseDTO(toolCategory);
    }

    public List<ToolCategoryResponseDTO> getAllToolCategories() {
        return toolCategoryUseCase.getAllToolCategories().stream()
                .map(toolCategoryPresenter::toToolCategoryResponseDTO)
                .toList();
    }

    public List<ToolCategoryResponseDTO> getAllToolCategoriesActive() {
        return toolCategoryUseCase.getAllToolCategoriesActive().stream()
                .map(toolCategoryPresenter::toToolCategoryResponseDTO)
                .toList();
    }

    public ToolCategoryResponseDTO updateToolCategory(UUID id, ToolCategoryRequestDTO requestDTO) {
        ToolCategory toolCategory = toolCategoryUseCase.updateToolCategory(id, requestDTO.getToolCategoryName());
        return toolCategoryPresenter.toToolCategoryResponseDTO(toolCategory);
    }

    public ToolCategoryResponseDTO reactivateToolCategory(UUID id) {
        ToolCategory toolCategory = toolCategoryUseCase.reactivateToolCategory(id);
        return toolCategoryPresenter.toToolCategoryResponseDTO(toolCategory);
    }

    public void logicallyDeleteToolCategory(UUID id) {
        toolCategoryUseCase.logicallyDeleteToolCategory(id);
    }
}
