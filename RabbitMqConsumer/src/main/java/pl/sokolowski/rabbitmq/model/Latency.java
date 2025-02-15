package pl.sokolowski.rabbitmq.model;

import jakarta.persistence.*;
import lombok.*;
import pl.sokolowski.rabbitmq.model.enums.QueueType;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Latency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long batchId;
    private Long startTime;
    private Long endTime;
    private Long producerLatency;
    private Long endToEndLatency;

    @Enumerated(value = EnumType.STRING)
    private QueueType queueType;
    private Long messageSize;
    private Long messageCount;

}
