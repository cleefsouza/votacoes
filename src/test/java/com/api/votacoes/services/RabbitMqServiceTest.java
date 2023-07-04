package com.api.votacoes.services;

import com.api.votacoes.dtos.response.ResultadoResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RabbitMqServiceTest {

    private RabbitMqService rabbitMqService;

    private ConnectionFactory connectionFactory;

    @Mock
    private RabbitTemplate rabbitTemplateMock;

    @Mock
    private Queue queueMock;

    @BeforeEach
    public void setUp() {
        connectionFactory = new CachingConnectionFactory("localhost", 5672);
        rabbitMqService = new RabbitMqService(connectionFactory, queueMock, rabbitTemplateMock);
    }

    @Test
    void testEnviarMensagemComResultadoDaVotacao() throws Exception {
        ResultadoResponseDto resultado = new ResultadoResponseDto(5, 6);
        resultado.montarResultado();

        var json = new ObjectMapper().writeValueAsString(resultado);

        when(queueMock.getName()).thenReturn("ms.resultado");

        rabbitMqService.enviarMensagem(resultado);

        verify(rabbitTemplateMock).convertAndSend("ms.resultado", json);
    }
}
