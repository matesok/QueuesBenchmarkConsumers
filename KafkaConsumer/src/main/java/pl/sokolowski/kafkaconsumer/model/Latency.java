package pl.sokolowski.kafkaconsumer.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import pl.sokolowski.activemq.model.enums.QueueType;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@ToString
@DynamicUpdate
@DynamicInsert
public class Latency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long batchId;
    private Long startTime;
    private Long endTime;
    @Transient
    private Long producerLatency;
    private Long endToEndLatency;

    @Enumerated(value = EnumType.STRING)
    private QueueType queueType;
    private Long messageSize;
    private Long messageCount;

}
