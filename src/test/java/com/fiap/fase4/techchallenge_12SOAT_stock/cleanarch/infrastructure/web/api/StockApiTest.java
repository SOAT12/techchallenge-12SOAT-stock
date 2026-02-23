package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.api;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.controller.StockController;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockApiTest {

    @Mock
    private StockController stockController;

    @InjectMocks
    private StockApi stockApi;

    @Test
    void createStock_Success() {
        CreateStockRequestDTO request = new CreateStockRequestDTO("Item", BigDecimal.ONE, 10, UUID.randomUUID());
        StockResponseDTO response = StockResponseDTO.builder().id(UUID.randomUUID()).build();

        when(stockController.createStock(any())).thenReturn(response);

        ResponseEntity<StockResponseDTO> result = stockApi.createStock(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void createStock_BadRequest() {
        when(stockController.createStock(any())).thenThrow(new IllegalArgumentException("Invalid"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> stockApi.createStock(new CreateStockRequestDTO()));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Invalid", ex.getReason());
    }

    @Test
    void getStockById() {
        UUID id = UUID.randomUUID();
        StockResponseDTO response = StockResponseDTO.builder().build();
        when(stockController.getStockById(id)).thenReturn(response);

        assertEquals(response, stockApi.getStockById(id));
    }

    @Test
    void getAllStockItems() {
        List<StockResponseDTO> list = List.of(StockResponseDTO.builder().build());
        when(stockController.getAllStockItems()).thenReturn(list);
        assertEquals(list, stockApi.getAllStockItems());
    }

    @Test
    void getAllStockItemsActive() {
        List<StockResponseDTO> list = List.of(StockResponseDTO.builder().build());
        when(stockController.getAllStockItemsActive()).thenReturn(list);
        assertEquals(list, stockApi.getAllStockItemsActive());
    }

    @Test
    void updateStock_Success() {
        UUID id = UUID.randomUUID();
        StockRequestDTO req = new StockRequestDTO("A", BigDecimal.ONE, true, 10, UUID.randomUUID());
        StockResponseDTO resp = StockResponseDTO.builder().build();
        when(stockController.updateStock(id, req)).thenReturn(resp);

        assertEquals(resp, stockApi.updateStock(id, req));
    }

    @Test
    void updateStock_BadRequest() {
        UUID id = UUID.randomUUID();
        when(stockController.updateStock(eq(id), any())).thenThrow(new IllegalArgumentException("Error"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> stockApi.updateStock(id, new StockRequestDTO()));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void deleteStock() {
        UUID id = UUID.randomUUID();
        stockApi.deleteStock(id);
        verify(stockController).logicallyDeleteStock(id);
    }

    @Test
    void reactivateStock() {
        UUID id = UUID.randomUUID();
        StockResponseDTO resp = StockResponseDTO.builder().build();
        when(stockController.reactivateStock(id)).thenReturn(resp);

        assertEquals(resp, stockApi.reactivateStock(id));
    }

    @Test
    void getStockAvailability() {
        StockAvailabilityRequestDTO req = new StockAvailabilityRequestDTO(List.of());
        StockAvailabilityResponseDTO resp = new StockAvailabilityResponseDTO(true, List.of());
        when(stockController.getStockAvailability(req)).thenReturn(resp);

        assertEquals(resp, stockApi.getStockAvailability(req));
    }
}
