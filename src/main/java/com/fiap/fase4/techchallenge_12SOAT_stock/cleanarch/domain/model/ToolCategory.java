package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model;

import java.util.Objects;
import java.util.UUID;

public class ToolCategory {

    private UUID id;
    private String toolCategoryName;
    private Boolean isActive;

    public ToolCategory(UUID id, String toolCategoryName, Boolean isActive) {
        this.id = id;
        this.toolCategoryName = toolCategoryName;
        this.isActive = isActive;
    }

    private ToolCategory(String toolCategoryName, Boolean isActive) {
        this.toolCategoryName = toolCategoryName;
        this.isActive = isActive;
    }

    public static ToolCategory create(String toolCategoryName) {
        if (toolCategoryName == null || toolCategoryName.isBlank()) {
            throw new IllegalArgumentException("A tool category name must not be null or blank");
        }
        return new ToolCategory(toolCategoryName, true);
    }

    public ToolCategory changeName(String newToolCategoryName) {
        if (Objects.isNull(newToolCategoryName)) {
            throw new IllegalArgumentException("Nome da categoria não pode ser nulo.");
        }
        this.toolCategoryName = newToolCategoryName;
        return this;
    }

    public ToolCategory deactivate() {
        if (Boolean.FALSE.equals(this.isActive)) {
            throw new IllegalStateException("Categoria já encontra-se desativada.");
        }
        this.isActive = false;
        return this;
    }

    public ToolCategory activate() {
        if (Boolean.TRUE.equals(this.isActive)) {
            throw new IllegalArgumentException("Categoria já encontra-se ativada.");
        }
        this.isActive = true;
        return this;
    }

    public UUID getId() { return id; }

    public String getToolCategoryName() { return toolCategoryName; }

    public Boolean getActive() { return isActive; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToolCategory that = (ToolCategory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
