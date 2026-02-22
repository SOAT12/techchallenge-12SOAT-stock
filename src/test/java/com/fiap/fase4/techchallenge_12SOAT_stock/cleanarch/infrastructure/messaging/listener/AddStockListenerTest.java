package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.useCases.StockUseCase;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.StockAddItemsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddStockListenerTest {

    @Mock
    private SqsClient sqsClient;

    @Mock
    private StockUseCase stockUseCase;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AddStockListener addStockListener;

    @BeforeEach
    void setup() {
        String queueUrl = "http://localhost:4566/000000000000/add-stock-queue";
        ReflectionTestUtils.setField(addStockListener, "queueUrl", queueUrl);
    }

    @Test
    void testListen_Success() throws Exception {
        // Arrange
        String receiptHandle = "receipt-handle";
        UUID itemId = UUID.randomUUID();
        String messageBody = "{\"items\":[{\"id\":\"" + itemId + "\",\"quantity\":5}]}";

        Message message = Message.builder()
                .body(messageBody)
                .receiptHandle(receiptHandle)
                .build();

        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
                .messages(message)
                .build();

        StockAddItemsDto mockDto = new StockAddItemsDto();
        StockAddItemsDto.StockUpdateDto item = new StockAddItemsDto.StockUpdateDto();
        ReflectionTestUtils.setField(item, "id", itemId);
        ReflectionTestUtils.setField(item, "quantity", 5);
        mockDto.setItems(List.of(item));

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(response);
        when(objectMapper.readValue(messageBody, StockAddItemsDto.class)).thenReturn(mockDto);

        // Act
        addStockListener.listen();

        // Assert
        verify(sqsClient).receiveMessage(any(ReceiveMessageRequest.class));
        verify(objectMapper).readValue(messageBody, StockAddItemsDto.class);
        verify(stockUseCase).addStock(itemId, 5);
        verify(sqsClient).deleteMessage(any(Consumer.class));
    }

    @Test
    void testListen_ExceptionProcessingMessage() throws Exception {
        // Arrange
        String receiptHandle = "receipt-handle-error";
        String messageBody = "invalid-json";

        Message message = Message.builder()
                .body(messageBody)
                .receiptHandle(receiptHandle)
                .build();

        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
                .messages(message)
                .build();

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(response);
        when(objectMapper.readValue(messageBody, StockAddItemsDto.class)).thenThrow(new RuntimeException("JSON error"));

        // Act
        addStockListener.listen();

        // Assert
        verify(sqsClient).receiveMessage(any(ReceiveMessageRequest.class));
        verify(objectMapper).readValue(messageBody, StockAddItemsDto.class);
        verify(stockUseCase, never()).addStock(any(UUID.class), any(Integer.class));
        verify(sqsClient, never()).deleteMessage(any(Consumer.class)); // Nao deleta em caso de erro
    }
}
