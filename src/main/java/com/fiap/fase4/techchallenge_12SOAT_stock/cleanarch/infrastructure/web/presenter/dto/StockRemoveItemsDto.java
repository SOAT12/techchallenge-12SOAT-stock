package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockRemoveItemsDto {

    private Long osId;
    private List<StockUpdateDto> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockUpdateDto {
        private UUID id;
        private Integer quantity;
    }

}
