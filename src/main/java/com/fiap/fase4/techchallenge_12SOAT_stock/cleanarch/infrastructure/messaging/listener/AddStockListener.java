package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.domain.useCases.StockUseCase;
import com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.infrastructure.web.presenter.dto.StockAddItemsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddStockListener {

    private final SqsClient sqsClient;
    private final StockUseCase stockUseCase;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.add-stock-queue-url}")
    private String queueUrl;

    @Scheduled(fixedDelay = 5000)
    public void listen() {
        var request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(10)
                .build();

        var response = sqsClient.receiveMessage(request);

        response.messages().forEach(msg -> {
            try {
                StockAddItemsDto dto = objectMapper.readValue(msg.body(), StockAddItemsDto.class);

                for (StockAddItemsDto.StockUpdateDto item : dto.getItems()) {
                    log.info("Adicionando estoque: productId={} quantity={}", item.getId(), item.getQuantity());
                    stockUseCase.addStock(item.getId(), item.getQuantity());
                }

                sqsClient.deleteMessage(builder -> builder
                        .queueUrl(queueUrl)
                        .receiptHandle(msg.receiptHandle()));

            } catch (Exception e) {
                log.error("Erro ao processar evento", e);
            }
        });
    }

}
