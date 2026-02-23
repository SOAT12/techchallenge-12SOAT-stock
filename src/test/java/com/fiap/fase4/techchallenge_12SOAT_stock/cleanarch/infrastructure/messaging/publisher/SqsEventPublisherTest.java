package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.messaging.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SqsEventPublisherTest {

    @Mock
    private SqsClient sqsClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SqsEventPublisher publisher;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(publisher, "osStatusQueueUrl", "http://sqs-url");
    }

    @Test
    void publishOsStatusUpdate_Success() throws Exception {
        Long osId = 1L;
        String status = "WAITING";
        String mockJson = "{\"osId\":1,\"newStatus\":\"WAITING\"}";

        when(objectMapper.writeValueAsString(any())).thenReturn(mockJson);
        when(sqsClient.sendMessage(any(SendMessageRequest.class))).thenReturn(SendMessageResponse.builder().build());

        publisher.publishOsStatusUpdate(osId, status);

        verify(objectMapper).writeValueAsString(any());
        verify(sqsClient).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void publishOsStatusUpdate_Exception() throws Exception {
        Long osId = 1L;
        String status = "WAITING";

        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("JSON error"));

        assertThrows(RuntimeException.class, () -> publisher.publishOsStatusUpdate(osId, status));
        verify(sqsClient, never()).sendMessage(any(SendMessageRequest.class));
    }
}
