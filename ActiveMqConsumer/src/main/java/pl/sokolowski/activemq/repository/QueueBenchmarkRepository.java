package pl.sokolowski.activemq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sokolowski.activemq.model.Latency;
import pl.sokolowski.activemq.model.enums.QueueType;

public interface QueueBenchmarkRepository extends JpaRepository<Latency, Long> {
    Latency findByBatchIdAndQueueType(long batchId, QueueType queueType);
}
