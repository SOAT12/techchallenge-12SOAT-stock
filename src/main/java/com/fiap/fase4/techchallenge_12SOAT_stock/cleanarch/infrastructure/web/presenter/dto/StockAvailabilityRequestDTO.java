package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAvailabilityRequestDTO {

    @NotEmpty(message = "A lista de itens não pode ser vazia.")
    private List<@Valid StockItemDTO> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockItemDTO {
        @NotNull(message = "O ID do item de estoque é obrigatório.")
        private UUID stockId;

        @NotNull(message = "A quantidade é obrigatória.")
        private Integer quantity;
    }
}
