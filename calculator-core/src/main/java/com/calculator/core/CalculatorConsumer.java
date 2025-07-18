package com.calculator.core;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.messaging.handler.annotation.Header;
import java.util.concurrent.CompletableFuture;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Service
public class CalculatorConsumer {

    private static final String REQUEST_TOPIC = "calculator-requests";

    private static final Logger log = LoggerFactory.getLogger(CalculatorConsumer.class);

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;
    private final CalculatorService calculatorService;

    public CalculatorConsumer(KafkaTemplate<String, KafkaMessage> kafkaTemplate,
            CalculatorService calculatorService) {
        this.kafkaTemplate = kafkaTemplate;
        this.calculatorService = calculatorService;
    }

    @KafkaListener(topics = REQUEST_TOPIC)
    public void listen(KafkaMessage request,
            @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId,
            @Header(KafkaHeaders.REPLY_TOPIC) byte[] replyTopic) {
        MDC.put("requestId", request.toString());
        log.info("Received Kafka message: {} {} {}", request.getA(), request.getOperation(),
                request.getB());

        try {
            BigDecimal result;

            switch (request.getOperation()) {
                case "sum":
                    result = calculatorService.sum(request.getA(), request.getB());
                    break;
                case "sub":
                    result = calculatorService.subtract(request.getA(), request.getB());
                    break;
                case "mul":
                    result = calculatorService.multiply(request.getA(), request.getB());
                    break;
                case "div":
                    result = calculatorService.divide(request.getA(), request.getB());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown operation: " + request.getOperation());
            }

            request.setResult(result);

        } finally {
            Message<KafkaMessage> replyMessage = MessageBuilder
                    .withPayload(request)
                    .setHeader(KafkaHeaders.TOPIC, replyTopic)
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();

            CompletableFuture<SendResult<String, KafkaMessage>> future = kafkaTemplate.send(replyMessage);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info(
                            "Mensagem de resposta enviada com sucesso para o tópico: {}, partição: {}, offset: {}, body: {}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset(),
                            request);
                } else {
                    log.error("Falha ao enviar mensagem de resposta para o tópico. Body: {}. Erro: {}", request,
                            ex.getMessage());
                }
                MDC.clear();
            });
        }
    }
}
