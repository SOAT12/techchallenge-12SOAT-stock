package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.controller;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.Stock;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.StockAvailabilityResult;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.useCases.StockUseCase;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.StockPresenter;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockControllerTest {

    @Mock
    private StockUseCase stockUseCase;

    @Mock
    private StockPresenter stockPresenter;

    @InjectMocks
    private StockController stockController;

    @Test
    void createStock() {
        CreateStockRequestDTO dto = new CreateStockRequestDTO("Martelo", BigDecimal.TEN, 5, UUID.randomUUID());
        Stock mockStock = Stock.builder().build();
        StockResponseDTO responseDTO = StockResponseDTO.builder().toolName("Martelo").build();

        when(stockUseCase.createStock(anyString(), any(BigDecimal.class), anyInt(), any(UUID.class)))
                .thenReturn(mockStock);
        when(stockPresenter.toStockResponseDTO(mockStock)).thenReturn(responseDTO);

        StockResponseDTO result = stockController.createStock(dto);

        assertNotNull(result);
        assertEquals("Martelo", result.getToolName());
        verify(stockUseCase).createStock(dto.getToolName(), dto.getValue(), dto.getQuantity(), dto.getToolCategoryId());
    }

    @Test
    void getStockById() {
        UUID id = UUID.randomUUID();
        Stock mockStock = Stock.builder().build();
        StockResponseDTO responseDTO = StockResponseDTO.builder().build();

        when(stockUseCase.findStockItemById(id)).thenReturn(mockStock);
        when(stockPresenter.toStockResponseDTO(mockStock)).thenReturn(responseDTO);

        StockResponseDTO result = stockController.getStockById(id);

        assertNotNull(result);
        verify(stockUseCase).findStockItemById(id);
    }

    @Test
    void getAllStockItems() {
        Stock mockStock = Stock.builder().build();
        when(stockUseCase.getAllStock()).thenReturn(List.of(mockStock));
        when(stockPresenter.toStockResponseDTO(mockStock)).thenReturn(StockResponseDTO.builder().build());

        List<StockResponseDTO> result = stockController.getAllStockItems();

        assertEquals(1, result.size());
        verify(stockUseCase).getAllStock();
    }

    @Test
    void getAllStockItemsActive() {
        Stock mockStock = Stock.builder().build();
        when(stockUseCase.getAllActiveStockItems()).thenReturn(List.of(mockStock));
        when(stockPresenter.toStockResponseDTO(mockStock)).thenReturn(StockResponseDTO.builder().build());

        List<StockResponseDTO> result = stockController.getAllStockItemsActive();

        assertEquals(1, result.size());
        verify(stockUseCase).getAllActiveStockItems();
    }

    @Test
    void updateStock() {
        UUID id = UUID.randomUUID();
        StockRequestDTO dto = new StockRequestDTO("Pá", BigDecimal.ONE, true, 10, UUID.randomUUID());
        Stock mockStock = Stock.builder().build();

        when(stockUseCase.updateStockItem(id, dto.getToolName(), dto.getValue(), dto.getQuantity(), dto.getActive(),
                dto.getToolCategoryId())).thenReturn(mockStock);
        when(stockPresenter.toStockResponseDTO(mockStock)).thenReturn(StockResponseDTO.builder().build());

        StockResponseDTO result = stockController.updateStock(id, dto);

        assertNotNull(result);
        verify(stockUseCase).updateStockItem(id, dto.getToolName(), dto.getValue(), dto.getQuantity(), dto.getActive(),
                dto.getToolCategoryId());
    }

    @Test
    void reactivateStock() {
        UUID id = UUID.randomUUID();
        Stock mockStock = Stock.builder().build();
        when(stockUseCase.reactivateStockItem(id)).thenReturn(mockStock);
        when(stockPresenter.toStockResponseDTO(mockStock)).thenReturn(StockResponseDTO.builder().build());

        StockResponseDTO result = stockController.reactivateStock(id);

        assertNotNull(result);
        verify(stockUseCase).reactivateStockItem(id);
    }

    @Test
    void logicallyDeleteStock() {
        UUID id = UUID.randomUUID();
        stockController.logicallyDeleteStock(id);
        verify(stockUseCase).inactivateStockItem(id);
    }

    @Test
    void getStockAvailability() {
        StockAvailabilityRequestDTO request = new StockAvailabilityRequestDTO();
        StockAvailabilityRequestDTO.StockItemDTO itemDTO = new StockAvailabilityRequestDTO.StockItemDTO();
        itemDTO.setStockId(UUID.randomUUID());
        itemDTO.setQuantity(5);
        request.setItems(List.of(itemDTO));

        StockAvailabilityResult mockResult = new StockAvailabilityResult(true, List.of());
        when(stockUseCase.getStockAvailability(any())).thenReturn(mockResult);

        StockAvailabilityResponseDTO responseDTO = new StockAvailabilityResponseDTO(true, List.of());
        when(stockPresenter.toStockAvailabilityResponseDTO(mockResult)).thenReturn(responseDTO);

        StockAvailabilityResponseDTO result = stockController.getStockAvailability(request);

        assertNotNull(result);
        assertTrue(result.isAvailable());
        verify(stockUseCase).getStockAvailability(any());
    }
}
