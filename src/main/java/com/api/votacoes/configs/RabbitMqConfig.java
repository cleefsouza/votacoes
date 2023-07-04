package com.api.votacoes.configs;

import com.api.votacoes.services.RabbitMqService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private static final String FILA = "ms.resultado";

    @Bean
    public Queue queue() {
        return new Queue(FILA, true);
    }

    @Bean
    public RabbitMqService rabbitMQService(ConnectionFactory conexao, Queue fila, RabbitTemplate rabbitTemplate) {
        return new RabbitMqService(conexao, fila, rabbitTemplate);
    }
}
