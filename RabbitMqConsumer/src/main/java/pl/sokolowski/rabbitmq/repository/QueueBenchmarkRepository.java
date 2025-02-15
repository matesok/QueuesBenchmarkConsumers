package pl.sokolowski.rabbitmq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sokolowski.rabbitmq.model.Latency;
import pl.sokolowski.rabbitmq.model.enums.QueueType;

public interface QueueBenchmarkRepository extends JpaRepository<Latency, Long> {
    Latency findByBatchIdAndQueueType(long batchId, QueueType queueType);
}
