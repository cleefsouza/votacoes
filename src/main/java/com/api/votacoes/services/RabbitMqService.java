package com.api.votacoes.services;

import com.api.votacoes.exceptions.MensagemNaoEnviadaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqService {

    private final ConnectionFactory conexao;

    private final Queue fila;

    public RabbitMqService(ConnectionFactory conexao, Queue fila) {
        this.conexao = conexao;
        this.fila = fila;
    }

    public void enviarMensagem(Object object) {
        try {
            var json = new ObjectMapper().writeValueAsString(object);
            RabbitTemplate template = new RabbitTemplate(conexao);
            template.convertAndSend(fila.getName(), json.getBytes());
        } catch (Exception e) {
            throw new MensagemNaoEnviadaException("Erro publicar mensagem no rabbitmq.");
        }
    }
}
