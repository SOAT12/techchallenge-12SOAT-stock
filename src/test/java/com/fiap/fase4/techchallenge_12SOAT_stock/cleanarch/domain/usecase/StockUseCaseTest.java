package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.usecase;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.Stock;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.StockAvailabilityItem;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.StockAvailabilityResult;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.useCases.StockUseCase;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.exception.NotFoundException;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.gateway.StockGateway;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.gateway.ToolCategoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class StockUseCaseTest {

    @Mock
    private StockGateway stockGateway;

    @Mock
    private ToolCategoryGateway toolCategoryGateway;

    @InjectMocks
    private StockUseCase stockUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class CreateStock {
        @Test
        void shouldCreateStock() {
            // Arrange
            UUID toolCategoryId = UUID.randomUUID();
            ToolCategory toolCategory = new ToolCategory(toolCategoryId, "Category", true);
            when(toolCategoryGateway.findById(toolCategoryId)).thenReturn(Optional.of(toolCategory));
            when(stockGateway.findByName(anyString())).thenReturn(Optional.empty());
            when(stockGateway.save(any(Stock.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            Stock result = stockUseCase.createStock("Tool", BigDecimal.TEN, 10, toolCategoryId);

            // Assert
            assertNotNull(result);
            verify(stockGateway).save(any(Stock.class));
        }

        @Test
        void shouldThrowIllegalArgumentExceptionWhenCategoryNotFound() {
            // Arrange
            UUID toolCategoryId = UUID.randomUUID();
            when(toolCategoryGateway.findById(toolCategoryId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> stockUseCase.createStock("Tool", BigDecimal.TEN, 10, toolCategoryId));
            verify(stockGateway, never()).save(any(Stock.class));
        }

        @Test
        void shouldThrowIllegalArgumentExceptionWhenItemAlreadyExists() {
            // Arrange
            UUID toolCategoryId = UUID.randomUUID();
            ToolCategory toolCategory = new ToolCategory(toolCategoryId, "Category", true);
            when(toolCategoryGateway.findById(toolCategoryId)).thenReturn(Optional.of(toolCategory));
            when(stockGateway.findByName(anyString())).thenReturn(Optional.of(Stock.builder().build()));

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> stockUseCase.createStock("Tool", BigDecimal.TEN, 10, toolCategoryId));
            verify(stockGateway, never()).save(any(Stock.class));
        }
    }

    @Nested
    class UpdateStockItem {
        @Test
        void shouldUpdateStockItem() {
            // Arrange
            UUID id = UUID.randomUUID();
            UUID toolCategoryId = UUID.randomUUID();
            ToolCategory toolCategory = new ToolCategory(toolCategoryId, "Category", true);
            Stock existingStock = Stock.builder().id(id).toolName("Old Tool").quantity(5).build();

            when(stockGateway.findById(id)).thenReturn(Optional.of(existingStock));
            when(toolCategoryGateway.findById(toolCategoryId)).thenReturn(Optional.of(toolCategory));
            when(stockGateway.save(any(Stock.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            Stock result = stockUseCase.updateStockItem(id, "New Tool", BigDecimal.ONE, 10, true, toolCategoryId);

            // Assert
            assertNotNull(result);
            assertEquals("New Tool", result.getToolName());
            assertEquals(10, result.getQuantity());
            verify(stockGateway).save(any(Stock.class));
        }

        @Test
        void shouldThrowNotFoundExceptionWhenStockItemNotFound() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(stockGateway.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> stockUseCase.updateStockItem(id, "New Tool", BigDecimal.ONE, 10, true, UUID.randomUUID()));
            verify(stockGateway, never()).save(any(Stock.class));
        }
    }

    @Nested
    class GetAllStock {
        @Test
        void shouldGetAllStock() {
            // Arrange
            when(stockGateway.findAll()).thenReturn(List.of(Stock.builder().build()));

            // Act
            List<Stock> result = stockUseCase.getAllStock();

            // Assert
            assertFalse(result.isEmpty());
            verify(stockGateway).findAll();
        }
    }

    @Nested
    class GetAllActiveStockItems {
        @Test
        void shouldGetAllActiveStockItems() {
            // Arrange
            when(stockGateway.findAllActive()).thenReturn(List.of(Stock.builder().build()));

            // Act
            List<Stock> result = stockUseCase.getAllActiveStockItems();

            // Assert
            assertFalse(result.isEmpty());
            verify(stockGateway).findAllActive();
        }
    }

    @Nested
    class FindStockItemById {
        @Test
        void shouldFindStockItemById() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(stockGateway.findActiveById(id)).thenReturn(Optional.of(Stock.builder().build()));

            // Act
            Stock result = stockUseCase.findStockItemById(id);

            // Assert
            assertNotNull(result);
            verify(stockGateway).findActiveById(id);
        }

        @Test
        void shouldThrowNotFoundExceptionWhenStockItemNotFound() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(stockGateway.findActiveById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> stockUseCase.findStockItemById(id));
        }
    }

    @Nested
    class InactivateStockItem {
        @Test
        void shouldInactivateStockItem() {
            // Arrange
            UUID id = UUID.randomUUID();
            Stock stock = Stock.builder().id(id).isActive(true).build();
            when(stockGateway.findById(id)).thenReturn(Optional.of(stock));
            when(stockGateway.save(any(Stock.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            stockUseCase.inactivateStockItem(id);

            // Assert
            assertFalse(stock.isActive());
            verify(stockGateway).save(any(Stock.class));
        }

        @Test
        void shouldThrowNotFoundExceptionWhenStockItemNotFound() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(stockGateway.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> stockUseCase.inactivateStockItem(id));
            verify(stockGateway, never()).save(any(Stock.class));
        }
    }

    @Nested
    class ReactivateStockItem {
        @Test
        void shouldReactivateStockItem() {
            // Arrange
            UUID id = UUID.randomUUID();
            Stock stock = Stock.builder().id(id).isActive(false).build();
            when(stockGateway.findById(id)).thenReturn(Optional.of(stock));
            when(stockGateway.save(any(Stock.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            Stock result = stockUseCase.reactivateStockItem(id);

            // Assert
            assertTrue(result.isActive());
            verify(stockGateway).save(any(Stock.class));
        }

        @Test
        void shouldThrowNotFoundExceptionWhenStockItemNotFound() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(stockGateway.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> stockUseCase.reactivateStockItem(id));
            verify(stockGateway, never()).save(any(Stock.class));
        }
    }

    @Nested
    class GetStockAvailability {
        @Test
        void shouldReturnAllAvailableTrue_whenAllItemsHaveSufficientStock() {
            // Arrange
            var stockId = UUID.randomUUID();
            Stock stock = Stock.builder().id(stockId).toolName("Tool").quantity(10).build();
            when(stockGateway.findActiveById(stockId))
                    .thenReturn(Optional.of(stock));

            var items = List.of(new StockAvailabilityItem(stockId, 5));

            // Act
            StockAvailabilityResult result =
                    stockUseCase.getStockAvailability(items);

            // Assert
            assertTrue(result.allAvailable());
            assertTrue(result.missingItems().isEmpty());
        }

        @Test
        void shouldReturnMissingItem_whenItemNotFound() {
            // Arrange
            var stockId = UUID.randomUUID();
            when(stockGateway.findActiveById(stockId))
                    .thenReturn(Optional.empty());

            var items = List.of(new StockAvailabilityItem(stockId, 5));

            // Act
            StockAvailabilityResult result =
                    stockUseCase.getStockAvailability(items);

            // Assert
            assertFalse(result.allAvailable());
            assertEquals(1, result.missingItems().size());

            var missing = result.missingItems().getFirst();
            assertEquals(stockId, missing.stockId());
            assertEquals("Item não encontrado", missing.name());
            assertEquals(5, missing.requiredQuantity());
            assertEquals(0, missing.availableQuantity());
        }

        @Test
        void shouldReturnMissingItem_whenStockIsInsufficient() {
            // Arrange
            var stockId = UUID.randomUUID();
            Stock stock = Stock.builder().id(stockId).toolName("Tool").quantity(3).build();
            when(stockGateway.findActiveById(stockId))
                    .thenReturn(Optional.of(stock));

            var items = List.of(new StockAvailabilityItem(stockId, 5));

            // Act
            StockAvailabilityResult result =
                    stockUseCase.getStockAvailability(items);

            // Assert
            assertFalse(result.allAvailable());
            assertEquals(1, result.missingItems().size());

            var missing = result.missingItems().getFirst();
            assertEquals(stockId, missing.stockId());
            assertEquals("Tool", missing.name());
            assertEquals(5, missing.requiredQuantity());
            assertEquals(3, missing.availableQuantity());
        }

        @Test
        void shouldReturnMultipleMissingItems_whenMoreThanOneProblemOccurs() {
            // Arrange
            var stockId = UUID.randomUUID();
            UUID id1 = UUID.randomUUID();
            UUID id2 = UUID.randomUUID();

            Stock stock = Stock.builder().id(stockId).toolName("Tool").quantity(1).build();

            when(stockGateway.findActiveById(id1))
                    .thenReturn(Optional.of(stock));

            when(stockGateway.findActiveById(id2))
                    .thenReturn(Optional.empty());

            var items = List.of(
                    new StockAvailabilityItem(id1, 5), // insuficiente
                    new StockAvailabilityItem(id2, 2)  // não encontrado
            );

            // Act
            StockAvailabilityResult result =
                    stockUseCase.getStockAvailability(items);

            // Assert
            assertFalse(result.allAvailable());
            assertEquals(2, result.missingItems().size());
        }
    }

}
