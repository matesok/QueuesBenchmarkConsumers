package pl.sokolowski.activemq.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import pl.sokolowski.activemq.model.Latency;
import pl.sokolowski.activemq.model.enums.QueueType;
import pl.sokolowski.activemq.repository.QueueBenchmarkRepository;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ActiveMqConsumer {
    private final ConcurrentMap<Long, AtomicInteger> receivedCounts = new ConcurrentHashMap<>();
    private final QueueBenchmarkRepository repository;

    @JmsListener(destination = "benchmark-queue", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(String message) {
        long recieviedTime = System.nanoTime();
        if (message.equals("warm-up")) {
            return;
        }

        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.wrap(messageBytes);
        long batchId = buffer.getLong();

        AtomicInteger counter = receivedCounts.computeIfAbsent(batchId, k -> new AtomicInteger(0));
        int currentCount = counter.incrementAndGet();

        Latency latencyRecord = repository.findByBatchIdAndQueueType(batchId, QueueType.ACTIVE_MQ);

        if (currentCount == latencyRecord.getMessageCount()) {
            latencyRecord.setEndTime(recieviedTime);
            long latencyNanos = latencyRecord.getEndTime() - latencyRecord.getStartTime();
            latencyRecord.setEndToEndLatency(latencyNanos / 1_000_000);
            repository.save(latencyRecord);
            receivedCounts.remove(batchId);
        }
    }
}

