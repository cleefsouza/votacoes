package com.api.votacoes.services;

import com.api.votacoes.dtos.response.ResultadoResponseDto;
import com.api.votacoes.models.AssociadoModel;
import com.api.votacoes.models.PautaModel;
import com.api.votacoes.models.VotoModel;
import com.api.votacoes.repositories.PautaRepository;
import com.api.votacoes.repositories.SessaoRepository;
import com.api.votacoes.repositories.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    private VotoService votoService;

    @Mock
    private VotoRepository votoRepositoryMock;

    @Mock
    private PautaRepository pautaRepositoryMock;

    @Mock
    private SessaoRepository sessaoRepositoryMock;

    private final UUID PAUTA_ID = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        votoService = new VotoService(votoRepositoryMock, pautaRepositoryMock, sessaoRepositoryMock);
    }

    @Test
    void testDeveSalvarUmVoto() {
        VotoModel votoModel = new VotoModel();
        votoModel.setPauta(new PautaModel());
        votoModel.setAssociado(new AssociadoModel());

        when(votoRepositoryMock.save(votoModel)).thenReturn(votoModel);
        VotoModel resultado = votoService.salvar(votoModel);

        verify(votoRepositoryMock).save(votoModel);
        assertEquals(votoModel, resultado);
    }

    @Test
    void testVerificarSeAssociadoJaVotou() {
        String cpf = "775.666.030-78";

        when(votoRepositoryMock.existsByPauta_IdAndAssociado_Cpf(PAUTA_ID, cpf)).thenReturn(true);
        boolean resultado = votoService.associadoJaVotou(PAUTA_ID, cpf);

        assertTrue(resultado);
    }

    @Test
    void testDeveRetornarOResultadoDaVotacao() {

        Object[] objects = new Object[2];
        objects[0] = 5;
        objects[1] = 6;

        List<Object[]> votos = new ArrayList<>();
        votos.add(objects);

        when(pautaRepositoryMock.existsById(PAUTA_ID)).thenReturn(true);
        when(sessaoRepositoryMock.existsByPauta_Id(PAUTA_ID)).thenReturn(true);
        when(votoRepositoryMock.buscarVotos(PAUTA_ID)).thenReturn(votos);

        ResultadoResponseDto resultado = votoService.buscarResultado(PAUTA_ID);

        assertNotNull(resultado);
        assertEquals("Pauta reprovada com 6 dos votos contra", resultado.getResultado());
        assertFalse(resultado.isAprovada());
    }

    @Test
    void testNaoDeveRetornarOResultadoDaVotacaoSemPauta() {
        when(pautaRepositoryMock.existsById(PAUTA_ID)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> votoService.buscarResultado(PAUTA_ID));
    }
}
