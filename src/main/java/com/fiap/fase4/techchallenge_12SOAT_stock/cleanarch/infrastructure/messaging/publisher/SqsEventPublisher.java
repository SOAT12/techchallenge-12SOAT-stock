package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.messaging.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsEventPublisher {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.os-status-queue-url}")
    private String osStatusQueueUrl;

    public void publishOsStatusUpdate(Long osId, String newStatus) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("osId", osId);
            payload.put("newStatus", newStatus);

            String message = objectMapper.writeValueAsString(payload);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(osStatusQueueUrl)
                    .messageBody(message)
                    .build();

            SendMessageResponse response = sqsClient.sendMessage(request);
            log.info("[SQS_EVENT_PUBLISHER] Mensagem publicada: {}", response);
        } catch (Exception e) {
            log.error("[SQS_EVENT_PUBLISHER] Erro ao publicar mensagem para atualizar status da OS", e);
            throw new RuntimeException("Erro ao publicar status da OS", e);
        }
    }

}
