package com.films.films.batch.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@Data
public class FilmReportProducerConfiguration {
    private final String directExchange = "films-report-generated";
    private final String routingKey = "films.report.mail";

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(directExchange);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory)
    {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setExchange(directExchange);
        template.setRoutingKey(routingKey);
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
