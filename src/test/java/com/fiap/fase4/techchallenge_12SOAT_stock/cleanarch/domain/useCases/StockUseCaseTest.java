package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.useCases;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.Stock;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.StockAvailabilityItem;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.StockAvailabilityResult;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.model.ToolCategory;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.gateway.StockGateway;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.gateway.ToolCategoryGateway;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.messaging.publisher.SqsEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockUseCaseTest {

    @Mock
    private StockGateway stockGateway;

    @Mock
    private ToolCategoryGateway toolCategoryGateway;

    @Mock
    private SqsEventPublisher sqsEventPublisher;

    @InjectMocks
    private StockUseCase stockUseCase;

    @Test
    void createStock_Success() {
        UUID categoryId = UUID.randomUUID();
        ToolCategory category = ToolCategory.create("Obras");

        when(toolCategoryGateway.findById(categoryId)).thenReturn(Optional.of(category));
        when(stockGateway.findByName("Tijolo")).thenReturn(Optional.empty());

        Stock mockStock = Stock.create("Tijolo", BigDecimal.TEN, 100, category);
        when(stockGateway.save(any(Stock.class))).thenReturn(mockStock);

        Stock result = stockUseCase.createStock("Tijolo", BigDecimal.TEN, 100, categoryId);

        assertNotNull(result);
        assertEquals("Tijolo", result.getToolName());
        verify(stockGateway).save(any(Stock.class));
    }

    @Test
    void createStock_CategoryNotFound() {
        when(toolCategoryGateway.findById(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> stockUseCase.createStock("Ferramenta", BigDecimal.ONE, 10, UUID.randomUUID()));
    }

    @Test
    void createStock_NameAlreadyExists() {
        UUID categoryId = UUID.randomUUID();
        when(toolCategoryGateway.findById(categoryId)).thenReturn(Optional.of(ToolCategory.create("O")));
        when(stockGateway.findByName("Existente")).thenReturn(Optional.of(Stock.builder().build()));

        assertThrows(IllegalArgumentException.class,
                () -> stockUseCase.createStock("Existente", BigDecimal.ONE, 10, categoryId));
    }

    @Test
    void updateStockItem_IncreaseStock() {
        UUID id = UUID.randomUUID();
        UUID catId = UUID.randomUUID();
        ToolCategory cat = ToolCategory.create("Cat");
        Stock existing = Stock.create("Old", BigDecimal.ONE, 10, cat);

        when(stockGateway.findById(id)).thenReturn(Optional.of(existing));
        when(toolCategoryGateway.findById(catId)).thenReturn(Optional.of(cat));
        when(stockGateway.save(any(Stock.class))).thenReturn(existing);

        // Aumentando quantity para 15 (+5)
        stockUseCase.updateStockItem(id, "Old", BigDecimal.TEN, 15, true, catId);

        assertEquals(15, existing.getQuantity());
        verify(stockGateway).save(existing);
    }

    @Test
    void updateStockItem_DecreaseStock() {
        UUID id = UUID.randomUUID();
        UUID catId = UUID.randomUUID();
        ToolCategory cat = ToolCategory.create("Cat");
        Stock existing = Stock.create("Old", BigDecimal.ONE, 10, cat);

        when(stockGateway.findById(id)).thenReturn(Optional.of(existing));
        when(toolCategoryGateway.findById(catId)).thenReturn(Optional.of(cat));
        when(stockGateway.save(any(Stock.class))).thenReturn(existing);

        // Diminuindo quantity para 5 (-5)
        stockUseCase.updateStockItem(id, "Old", BigDecimal.TEN, 5, true, catId);

        assertEquals(5, existing.getQuantity());
        verify(stockGateway).save(existing);
    }

    @Test
    void updateStockItem_NameConflict() {
        UUID id = UUID.randomUUID();
        UUID outraId = UUID.randomUUID();
        Stock existing = Stock.builder().id(id).toolName("Old").build();
        Stock outro = Stock.builder().id(outraId).toolName("New Name").build();

        when(stockGateway.findById(id)).thenReturn(Optional.of(existing));
        when(stockGateway.findByName("New Name")).thenReturn(Optional.of(outro));

        assertThrows(IllegalArgumentException.class,
                () -> stockUseCase.updateStockItem(id, "New Name", BigDecimal.ONE, 10, true, UUID.randomUUID()));
    }

    @Test
    void addStock() {
        UUID id = UUID.randomUUID();
        Stock stock = Stock.create("N", BigDecimal.ONE, 10, ToolCategory.create("C"));
        when(stockGateway.findById(id)).thenReturn(Optional.of(stock));

        stockUseCase.addStock(id, 5);

        assertEquals(15, stock.getQuantity());
        verify(stockGateway).save(stock);
    }

    @Test
    void removeStock_Success() {
        UUID id = UUID.randomUUID();
        Stock stock = Stock.create("N", BigDecimal.ONE, 10, ToolCategory.create("C"));
        when(stockGateway.findById(id)).thenReturn(Optional.of(stock));

        stockUseCase.removeStock(1L, id, 5);

        assertEquals(5, stock.getQuantity());
        verify(sqsEventPublisher, never()).publishOsStatusUpdate(any(), any());
        verify(stockGateway).save(stock);
    }

    @Test
    void removeStock_InsufficientQuantity() {
        UUID id = UUID.randomUUID();
        Stock stock = Stock.create("N", BigDecimal.ONE, 10, ToolCategory.create("C"));
        when(stockGateway.findById(id)).thenReturn(Optional.of(stock));

        // Removendo mais do que tem dispara IllegalArgumentException que é catched.
        stockUseCase.removeStock(1L, id, 15);

        // O estoque permanece intocavel em 10, mas publish acontece
        assertEquals(10, stock.getQuantity());
        verify(sqsEventPublisher).publishOsStatusUpdate(1L, "WAITING_FOR_STOCK");
        verify(stockGateway).save(stock);
    }

    @Test
    void getAllStock() {
        when(stockGateway.findAll()).thenReturn(List.of(Stock.builder().build()));
        assertEquals(1, stockUseCase.getAllStock().size());
    }

    @Test
    void getAllActiveStockItems() {
        when(stockGateway.findAllActive()).thenReturn(List.of(Stock.builder().build()));
        assertEquals(1, stockUseCase.getAllActiveStockItems().size());
    }

    @Test
    void findStockItemById() {
        UUID id = UUID.randomUUID();
        when(stockGateway.findActiveById(id)).thenReturn(Optional.of(Stock.builder().build()));
        assertNotNull(stockUseCase.findStockItemById(id));
    }

    @Test
    void inactivateStockItem() {
        UUID id = UUID.randomUUID();
        Stock stock = Stock.create("N", BigDecimal.ONE, 10, ToolCategory.create("C"));
        when(stockGateway.findById(id)).thenReturn(Optional.of(stock));

        stockUseCase.inactivateStockItem(id);

        assertFalse(stock.isActive());
        verify(stockGateway).save(stock);
    }

    @Test
    void reactivateStockItem() {
        UUID id = UUID.randomUUID();
        Stock stock = Stock.create("N", BigDecimal.ONE, 10, ToolCategory.create("C"));
        stock.deactivate();
        when(stockGateway.findById(id)).thenReturn(Optional.of(stock));

        stockUseCase.reactivateStockItem(id);

        assertTrue(stock.isActive());
        verify(stockGateway).save(stock);
    }

    @Test
    void getStockAvailability() {
        // Arrange
        UUID idAvailable = UUID.randomUUID();
        UUID idMissing = UUID.randomUUID();
        UUID idNotFound = UUID.randomUUID();

        Stock stockAvailable = Stock.builder().id(idAvailable).toolName("A").quantity(10).build();
        Stock stockMissing = Stock.builder().id(idMissing).toolName("B").quantity(2).build();

        when(stockGateway.findActiveById(idAvailable)).thenReturn(Optional.of(stockAvailable));
        when(stockGateway.findActiveById(idMissing)).thenReturn(Optional.of(stockMissing));
        when(stockGateway.findActiveById(idNotFound)).thenReturn(Optional.empty());

        List<StockAvailabilityItem> requestItems = List.of(
                new StockAvailabilityItem(idAvailable, 5), // ok
                new StockAvailabilityItem(idMissing, 5), // missing 3
                new StockAvailabilityItem(idNotFound, 5) // missing 5 (not found)
        );

        // Act
        StockAvailabilityResult result = stockUseCase.getStockAvailability(requestItems);

        // Assert
        assertFalse(result.allAvailable());
        assertEquals(2, result.missingItems().size());

        // Verifica o primeiro missing (que é o idMissing por passar da capacidade)
        StockAvailabilityResult.MissingStockItem missing1 = result.missingItems().stream()
                .filter(m -> m.stockId().equals(idMissing)).findFirst().get();
        assertEquals(2, missing1.availableQuantity());

        // Verifica o segundo (not found)
        StockAvailabilityResult.MissingStockItem missingNotFound = result.missingItems().stream()
                .filter(m -> m.stockId().equals(idNotFound)).findFirst().get();
        assertEquals(0, missingNotFound.availableQuantity());
        assertEquals("Item não encontrado", missingNotFound.name());
    }
}
