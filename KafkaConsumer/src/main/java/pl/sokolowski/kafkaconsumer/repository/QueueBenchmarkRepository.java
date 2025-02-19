package pl.sokolowski.kafkaconsumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sokolowski.activemq.model.enums.QueueType;
import pl.sokolowski.kafkaconsumer.model.Latency;

public interface QueueBenchmarkRepository extends JpaRepository<Latency, Long> {
   Latency findByBatchIdAndQueueType(long batchId, QueueType queueType);
}
