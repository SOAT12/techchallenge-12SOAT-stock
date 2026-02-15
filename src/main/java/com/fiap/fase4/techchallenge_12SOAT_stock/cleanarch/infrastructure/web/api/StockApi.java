package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.api;

import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.controller.StockController;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stock")
@Tag(name = "Estoque", description = "API para gerenciar itens em estoque")
public class StockApi {

    private final StockController stockController;

    @Operation(summary = "Cria um novo item de estoque")
    @ApiResponse(responseCode = "201", description = "Item de estoque criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @PostMapping
    public ResponseEntity<StockResponseDTO> createStock(@Valid @RequestBody CreateStockRequestDTO requestDTO) {
        try {
            StockResponseDTO created = stockController.createStock(requestDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Obtém um item de estoque pelo ID")
    @ApiResponse(responseCode = "200", description = "Item de estoque encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    @GetMapping("/{id}")
    public StockResponseDTO getStockById(@PathVariable UUID id) {
        return stockController.getStockById(id);
    }

    @Operation(summary = "Lista todos os itens de estoque")
    @ApiResponse(responseCode = "200", description = "Lista de itens de estoque retornada com sucesso")
    @GetMapping("/all")
    public List<StockResponseDTO> getAllStockItems() {
        return stockController.getAllStockItems();
    }

    @Operation(summary = "Lista todos os itens de estoque ativos")
    @ApiResponse(responseCode = "200", description = "Lista de itens de estoque ativos retornada com sucesso")
    @GetMapping
    public List<StockResponseDTO> getAllStockItemsActive() {
        return stockController.getAllStockItemsActive();
    }

    @Operation(summary = "Atualiza um item de estoque existente")
    @ApiResponse(responseCode = "200", description = "Item de estoque atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    @PutMapping("/{id}")
    public StockResponseDTO updateStock(@PathVariable UUID id, @Valid @RequestBody StockRequestDTO requestDTO) {
        try {
            return stockController.updateStock(id, requestDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Deleta um item de estoque (exclusão lógica)")
    @ApiResponse(responseCode = "204", description = "Item de estoque deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable UUID id) {
        stockController.logicallyDeleteStock(id);
    }

    @Operation(summary = "Reativa um item de estoque")
    @ApiResponse(responseCode = "200", description = "Item de estoque reativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    @PatchMapping("/{id}/reactivate")
    public StockResponseDTO reactivateStock(@PathVariable UUID id) {
        return stockController.reactivateStock(id);
    }

    @Operation(summary = "Verifica disponibilidade de itens no estoque")
    @ApiResponse(responseCode = "200", description = "Disponibilidade verificada com sucesso")
    @PostMapping("/availability")
    public StockAvailabilityResponseDTO getStockAvailability(@Valid @RequestBody StockAvailabilityRequestDTO request) {
        return stockController.getStockAvailability(request);
    }
}
