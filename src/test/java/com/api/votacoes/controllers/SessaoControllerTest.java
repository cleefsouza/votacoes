package com.api.votacoes.controllers;

import com.api.votacoes.dtos.request.SessaoRequestDto;
import com.api.votacoes.models.PautaModel;
import com.api.votacoes.models.SessaoModel;
import com.api.votacoes.services.interfaces.IPautaService;
import com.api.votacoes.services.interfaces.ISessaoService;
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
import java.util.Optional;
import java.util.UUID;

import static com.api.votacoes.utils.ConstantesUtils.PAUTA_URL_ID;
import static com.api.votacoes.utils.ConstantesUtils.SESSAO_URL;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SessaoControllerTest {

    @Mock
    private ISessaoService sessaoService;

    @Mock
    private IPautaService pautaService;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    private SessaoModel sessaoModel;

    private final UUID PAUTA_ID = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SessaoController(sessaoService, pautaService)).build();
        objectMapper = new ObjectMapper();
        sessaoModel = this.buildSessao();
    }

    @Test
    void testDeveAceitarRequestESalvarUmaSessao() throws Exception {
        SessaoRequestDto sessaoRequestDto = new SessaoRequestDto(1, false);
        String json = objectMapper.writeValueAsString(sessaoRequestDto);

        when(sessaoService.existeSessao(PAUTA_ID)).thenReturn(false);
        when(pautaService.buscarPorId(PAUTA_ID)).thenReturn(Optional.of(sessaoModel.getPauta()));
        when(sessaoService.salvar(sessaoModel)).thenReturn(sessaoModel);
        doNothing().when(sessaoService).iniciarSessao(sessaoModel);

        String pautaUrlId = PAUTA_URL_ID.replace("{pautaId}", String.valueOf(PAUTA_ID));

        mockMvc.perform(post(pautaUrlId + SESSAO_URL + "/iniciar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.duracao").value(sessaoModel.getDuracao()))
                .andExpect(jsonPath("$.encerrada").value(sessaoModel.isEncerrada()))
                .andExpect(jsonPath("$.dataCriacao").isNotEmpty());

        verify(sessaoService, times(1)).salvar(sessaoModel);
    }

    private SessaoModel buildSessao() {
        SessaoModel sessaoModelBuild = new SessaoModel();
        sessaoModelBuild.setDuracao(1);
        sessaoModelBuild.setDataCriacao(LocalDate.now(ZoneId.of("UTC")));

        PautaModel pautaModel = new PautaModel();
        pautaModel.setId(PAUTA_ID);
        pautaModel.setTitulo("Pauta Teste 1");
        pautaModel.setDescricao("Pauta para testar o endpoint post do recurso /pauta/id/sessao/iniciar");
        pautaModel.setDataCriacao(LocalDate.now(ZoneId.of("UTC")));

        sessaoModelBuild.setPauta(pautaModel);

        return sessaoModelBuild;
    }
}
