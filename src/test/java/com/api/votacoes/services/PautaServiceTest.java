package com.api.votacoes.services;

import com.api.votacoes.models.PautaModel;
import com.api.votacoes.repositories.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    private PautaService pautaService;

    @Mock
    private PautaRepository pautaRepositoryMock;

    @BeforeEach
    public void setUp() {
        pautaService = new PautaService(pautaRepositoryMock);
    }

    @Test
    void testDeveSalvarNovaPauta() {
        PautaModel pautaModel = new PautaModel();
        pautaModel.setTitulo("Nova pauta");

        when(pautaRepositoryMock.save(pautaModel)).thenReturn(pautaModel);
        PautaModel resultado = pautaService.salvar(pautaModel);

        verify(pautaRepositoryMock).save(pautaModel);
        assertEquals(pautaModel, resultado);
    }

    @Test
    void testVerificarSePautaJaExiste() {
        String titulo = "Pauta existente";

        when(pautaRepositoryMock.existsByTitulo(titulo)).thenReturn(true);
        boolean resultado = pautaService.pautaJaExiste(titulo);

        assertTrue(resultado);
    }

    @Test
    void testDeveListarPautas() {
        Page<PautaModel> pagePautas = Page.empty();
        Pageable pageable = Pageable.ofSize(5).withPage(0);

        when(pautaRepositoryMock.findAll(pageable)).thenReturn(pagePautas);
        Page<PautaModel> resultado = pautaService.buscarPautas(pageable);

        assertEquals(pagePautas, resultado);
        assertEquals(pagePautas.getSize(), resultado.getSize());
        assertEquals(pagePautas.getContent(), resultado.getContent());
    }

    @Test
    void testDeveBuscarPautaPorId() {
        UUID pautaId = UUID.randomUUID();
        Optional<PautaModel> pautaMock = Optional.of(new PautaModel());

        when(pautaRepositoryMock.findById(pautaId)).thenReturn(pautaMock);
        Optional<PautaModel> resultado = pautaService.buscarPorId(pautaId);

        assertNotNull(resultado);
        assertEquals(pautaMock, resultado);
    }
}
