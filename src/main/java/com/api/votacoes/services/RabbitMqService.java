package com.api.votacoes.services;

import com.api.votacoes.exceptions.MensagemNaoEnviadaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMqService {

    private final ConnectionFactory conexao;

    private final Queue fila;

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqService(ConnectionFactory conexao, Queue fila, RabbitTemplate rabbitTemplate) {
        this.conexao = conexao;
        this.fila = fila;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensagem(Object object) {
        try {
            var json = new ObjectMapper().writeValueAsString(object);
            rabbitTemplate.setConnectionFactory(conexao);
            rabbitTemplate.convertAndSend(fila.getName(), json);
            log.info(String.format("Mensagem enviada com sucesso para a fila \"%s\"", fila.getName()));
        } catch (Exception e) {
            log.error("Ocorreu um erro ao tentar enviar a mensagem para fila no rabbitmq");
            throw new MensagemNaoEnviadaException("Erro enviar mensagem para fila no rabbitmq");
        }
    }
}
