package com.calculator.rest;

import com.calculator.core.KafkaMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final ReplyingKafkaTemplate<String, KafkaMessage, KafkaMessage> replyingKafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(CalculatorController.class);

    public CalculatorController(ReplyingKafkaTemplate<String, KafkaMessage, KafkaMessage> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UNKNOWN");

        try {
            replyingKafkaTemplate.getProducerFactory().createProducer().partitionsFor(CalculatorConfig.REQUEST_TOPIC);
            status.put("status", "UP");
        } catch (Exception e) {
            status.put("status", "DOWN");
        }

        return ResponseEntity.ok(status);
    }

    @PostMapping("/calculate")
    public ResponseEntity<?> calculate(@RequestBody CalculatorRequest request) throws Exception {
        UUID requestId = request.getRequestId() != null ? request.getRequestId() : UUID.randomUUID();
        MDC.put("requestId", requestId.toString());
        log.info("Received request [{}]: {} {} {}", request.getA(),
                request.getB());

        KafkaMessage message = new KafkaMessage(
                requestId,
                request.getOperation(),
                request.getA(),
                request.getB());

        ProducerRecord<String, KafkaMessage> record = new ProducerRecord<>(
                CalculatorConfig.REQUEST_TOPIC,
                requestId.toString(),
                message);

        try {
            RequestReplyFuture<String, KafkaMessage, KafkaMessage> replyFuture = replyingKafkaTemplate
                    .sendAndReceive(record);
            ConsumerRecord<String, KafkaMessage> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);

            KafkaMessage response = consumerRecord.value();

            log.info("Result={}", response.getResult());
            return ResponseEntity.ok()
                    .header("X-Request-ID", requestId.toString())
                    .body(Collections.singletonMap("result", response.getResult()));

        } catch (Exception ex) {
            log.error("Request [{}] internal error: {}", requestId, ex.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", ex.getMessage());
            return ResponseEntity.internalServerError()
                    .header("X-Request-ID", requestId.toString())
                    .body(error);
        } finally {
            MDC.clear();
        }
    }
}
