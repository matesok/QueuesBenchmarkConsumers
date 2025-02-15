package pl.sokolowski.kafkaconsumer.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    @KafkaListener(topics = "benchmark-topic", groupId = "benchmark-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }

}
