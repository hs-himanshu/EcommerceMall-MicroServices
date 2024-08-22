package com.example.userservice.configs;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;


@Configuration
@AllArgsConstructor
public class KafkaProducerConfig {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

}
