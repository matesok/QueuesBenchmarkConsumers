package pl.sokolowski.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.sokolowski.rabbitmq.model.Latency;
import pl.sokolowski.rabbitmq.model.enums.QueueType;
import pl.sokolowski.rabbitmq.repository.QueueBenchmarkRepository;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class RabbitMqReceiver {
    private final ConcurrentMap<Long, AtomicInteger> receivedCounts = new ConcurrentHashMap<>();
    private final QueueBenchmarkRepository repository;

    @RabbitListener(queues = "benchmark-queue")
    public void receiveMessage(String message) {
        if (message.equals("warm-up")) {
            return;
        }

        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.wrap(messageBytes);
        long batchId = buffer.getLong();

        AtomicInteger counter = receivedCounts.computeIfAbsent(batchId, k -> new AtomicInteger(0));
        int currentCount = counter.incrementAndGet();

        Latency latencyRecord = repository.findByBatchIdAndQueueType(batchId, QueueType.RABBIT_MQ);

        if (currentCount == latencyRecord.getMessageCount()) {
            latencyRecord.setEndTime(System.nanoTime());

            long latencyNanos = latencyRecord.getEndTime() - latencyRecord.getStartTime();
            latencyRecord.setEndToEndLatency(latencyNanos / 1_000_000);
            repository.save(latencyRecord);

            receivedCounts.remove(batchId);
        }
    }
}

