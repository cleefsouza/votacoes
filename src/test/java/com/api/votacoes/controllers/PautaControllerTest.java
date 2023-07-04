package com.api.votacoes.controllers;

import com.api.votacoes.dtos.request.PautaRequestDto;
import com.api.votacoes.models.PautaModel;
import com.api.votacoes.services.interfaces.IPautaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.ZoneId;

import static com.api.votacoes.utils.ConstantesUtils.PAUTA_URL;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PautaControllerTest {

    @Mock
    private IPautaService pautaService;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    private PautaModel pautaModel;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PautaController(pautaService)).build();
        objectMapper = new ObjectMapper();

        pautaModel = new PautaModel();
        pautaModel.setTitulo("Pauta Teste 1");
        pautaModel.setDescricao("Pauta para testar o endpoint post do recurso /pauta/cadastrar");
        pautaModel.setDataCriacao(LocalDate.now(ZoneId.of("UTC")));
    }

    @Test
    void testDeveAceitarRequestESalvarUmaPauta() throws Exception {
        PautaRequestDto pautaRequestDto = new PautaRequestDto("Pauta Teste 1", "Pauta para testar o endpoint post do recurso /pauta/cadastrar");
        String json = objectMapper.writeValueAsString(pautaRequestDto);

        when(pautaService.salvar(pautaModel)).thenReturn(pautaModel);

        mockMvc.perform(post(PAUTA_URL + "/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value(pautaModel.getTitulo()))
                .andExpect(jsonPath("$.dataCriacao").isNotEmpty());

        verify(pautaService, times(1)).salvar(pautaModel);
    }
}
