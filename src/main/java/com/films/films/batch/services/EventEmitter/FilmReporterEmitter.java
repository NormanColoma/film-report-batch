package com.films.films.batch.services.EventEmitter;

import com.films.films.batch.configuration.FilmReportProducerConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class FilmReporterEmitter {
    private final FilmReportProducerConfiguration filmReportProducerConfiguration;
    private final RabbitTemplate rabbitTemplate;

    public void emit(String message) {
        rabbitTemplate.convertAndSend(filmReportProducerConfiguration.getDirectExchange(), filmReportProducerConfiguration.getRoutingKey(), message);
    }
}
