package com.api.votacoes.services;

import com.api.votacoes.models.AssociadoModel;
import com.api.votacoes.repositories.AssociadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssociadoServiceTest {

    private AssociadoService associadoService;

    @Mock
    private AssociadoRepository associadoRepositoryMock;

    @BeforeEach
    public void setUp() {
        associadoService = new AssociadoService(associadoRepositoryMock);
    }

    @Test
    void testDeveSalvarNovoAssociado() {
        AssociadoModel associadoModel = new AssociadoModel();
        associadoModel.setCpf("775.666.030-78");

        when(associadoRepositoryMock.save(associadoModel)).thenReturn(associadoModel);
        AssociadoModel resultado = associadoService.salvar(associadoModel);

        verify(associadoRepositoryMock).save(associadoModel);
        assertEquals(associadoModel, resultado);
    }

    @Test
    void testVerificarSeAssociadoJaEstaCadastrado() {
        String cpf = "775.666.030-78";

        when(associadoRepositoryMock.existsByCpf(cpf)).thenReturn(false);
        boolean resultado = associadoService.associadoJaExiste(cpf);

        assertFalse(resultado);
    }

    @Test
    void testDeveBuscarAssociadoPorCpf() {
        String cpf = "775.666.030-78";
        Optional<AssociadoModel> associadoMock = Optional.of(new AssociadoModel());

        when(associadoRepositoryMock.findByCpf(cpf)).thenReturn(associadoMock);
        Optional<AssociadoModel> resultado = associadoService.buscarPorCpf(cpf);

        assertNotNull(resultado);
        assertEquals(associadoMock, resultado);
    }
}
