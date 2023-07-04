package com.api.votacoes.services;

import com.api.votacoes.dtos.response.ResultadoResponseDto;
import com.api.votacoes.models.PautaModel;
import com.api.votacoes.models.SessaoModel;
import com.api.votacoes.repositories.SessaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoServiceTest {

    private SessaoService sessaoService;

    @Mock
    private SessaoRepository sessaoRepositoryMock;

    @Mock
    private VotoService votoServiceMock;

    @Mock
    private RabbitMqService rabbitMqServiceMock;

    @Mock
    private ScheduledExecutorService executorServiceMock;

    private final UUID PAUTA_ID = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        sessaoService = new SessaoService(sessaoRepositoryMock, votoServiceMock, rabbitMqServiceMock);
        sessaoService.setExecutorService(executorServiceMock);
    }

    @Test
    void testVerificarSeSessaoEstaEncerrada() {

        when(sessaoRepositoryMock.existsByPauta_Id(PAUTA_ID)).thenReturn(true);
        when(sessaoRepositoryMock.estaEncerrada(PAUTA_ID)).thenReturn(true);

        boolean resultado = sessaoService.estaEncerrada(PAUTA_ID);

        assertTrue(resultado);
        verify(sessaoRepositoryMock, times(1)).existsByPauta_Id(PAUTA_ID);
        verify(sessaoRepositoryMock, times(1)).estaEncerrada(PAUTA_ID);
    }

    @Test
    void testDeveRetornarUmErroCasoNaoExistaSessao() {

        when(sessaoRepositoryMock.existsByPauta_Id(PAUTA_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> sessaoService.estaEncerrada(PAUTA_ID));

        verify(sessaoRepositoryMock, times(1)).existsByPauta_Id(PAUTA_ID);
        verify(sessaoRepositoryMock, never()).estaEncerrada(PAUTA_ID);
    }

    @Test
    void testVerificarSeSessaoExiste() {

        when(sessaoRepositoryMock.existsByPauta_Id(PAUTA_ID)).thenReturn(true);
        boolean resultado = sessaoService.existeSessao(PAUTA_ID);

        assertTrue(resultado);
    }

    @Test
    void testDeveSalvarNovaSessao() {

        SessaoModel sessaoModel = new SessaoModel();
        sessaoModel.setPauta(new PautaModel());

        when(sessaoRepositoryMock.save(sessaoModel)).thenReturn(sessaoModel);
        SessaoModel resultado = sessaoService.salvar(sessaoModel);

        verify(sessaoRepositoryMock).save(sessaoModel);
        assertEquals(sessaoModel, resultado);
    }

    @Test
    void testDeveEncerrarUmaSessao() {

        SessaoModel sessao = new SessaoModel();
        sessao.setId(UUID.randomUUID());
        sessao.setDuracao(1);

        PautaModel pauta = new PautaModel();
        pauta.setId(PAUTA_ID);

        sessao.setPauta(pauta);

        ResultadoResponseDto resultado = new ResultadoResponseDto(3, 5);
        when(sessaoRepositoryMock.save(sessao)).thenReturn(sessao);
        when(votoServiceMock.buscarResultado(pauta.getId())).thenReturn(resultado);

        sessaoService.encerrarSessao(sessao);

        verify(sessaoRepositoryMock, times(1)).save(sessao);
        verify(rabbitMqServiceMock, times(1)).enviarMensagem(resultado);
    }
}
