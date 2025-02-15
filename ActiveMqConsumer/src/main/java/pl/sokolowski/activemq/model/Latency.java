package pl.sokolowski.activemq.model;

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

    @ElementCollection
    private List<Long> perMessageLatencies = new ArrayList<>();
    public void addPerMessageLatency(Long latency) {
        perMessageLatencies.add(latency);
    }

    public Double getAverageLatency() {
        return perMessageLatencies.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
    }

    public Long getMinLatency() {
        return perMessageLatencies.stream()
                .mapToLong(Long::longValue)
                .min()
                .orElse(0L);
    }

    public Long getMaxLatency() {
        return perMessageLatencies.stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);
    }

    public Long getP95Latency() {
        if (perMessageLatencies.isEmpty()) return 0L;
        perMessageLatencies.sort(Long::compare);
        int index = (int) Math.ceil(0.95 * perMessageLatencies.size()) - 1;
        return perMessageLatencies.get(Math.max(index, 0));
    }

    public Long getP99Latency() {
        if (perMessageLatencies.isEmpty()) return 0L;
        perMessageLatencies.sort(Long::compare);
        int index = (int) Math.ceil(0.99 * perMessageLatencies.size()) - 1;
        return perMessageLatencies.get(Math.max(index, 0));
    }
}
