package com.api.votacoes.configs;

import com.api.votacoes.services.RabbitMqService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private static final String FILA = "ms.sessao";

    @Bean
    public Queue queue() {
        return new Queue(FILA, true);
    }

    @Bean
    public RabbitMqService rabbitMQService(ConnectionFactory conexao, Queue fila) {
        return new RabbitMqService(conexao, fila);
    }
}
