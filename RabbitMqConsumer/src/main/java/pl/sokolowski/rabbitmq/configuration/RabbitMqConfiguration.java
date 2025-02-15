package pl.sokolowski.rabbitmq.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    @Bean
    public Queue benchmarkQueue() {
        return new Queue("benchmark-queue", false);
    }
}
