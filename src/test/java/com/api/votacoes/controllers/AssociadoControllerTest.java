package com.api.votacoes.controllers;

import com.api.votacoes.dtos.request.AssociadoRequestDto;
import com.api.votacoes.models.AssociadoModel;
import com.api.votacoes.services.interfaces.IAssociadoService;
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

import static com.api.votacoes.utils.ConstantesUtils.ASSOCIADO_URL;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AssociadoControllerTest {

    @Mock
    private IAssociadoService associadoService;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    private AssociadoModel associadoModel;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AssociadoController(associadoService)).build();
        objectMapper = new ObjectMapper();

        associadoModel = new AssociadoModel();
        associadoModel.setNome("Associado Teste 1");
        associadoModel.setCpf("775.666.030-78");
        associadoModel.setDataCriacao(LocalDate.now(ZoneId.of("UTC")));
    }

    @Test
    void testDeveAceitarRequestESalvarUmAssociado() throws Exception {
        AssociadoRequestDto associadoRequestDto = new AssociadoRequestDto("Associado Teste 1", "775.666.030-78");
        String json = objectMapper.writeValueAsString(associadoRequestDto);

        when(associadoService.salvar(associadoModel)).thenReturn(associadoModel);

        mockMvc.perform(post(ASSOCIADO_URL + "/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(associadoModel.getNome()))
                .andExpect(jsonPath("$.cpf").value(associadoModel.getCpf()))
                .andExpect(jsonPath("$.dataCriacao").isNotEmpty());

        verify(associadoService, times(1)).salvar(associadoModel);
    }
}
