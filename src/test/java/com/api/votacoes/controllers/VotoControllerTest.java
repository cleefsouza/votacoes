package com.api.votacoes.controllers;

import com.api.votacoes.dtos.request.VotoRequestDto;
import com.api.votacoes.dtos.response.ResultadoResponseDto;
import com.api.votacoes.models.AssociadoModel;
import com.api.votacoes.models.PautaModel;
import com.api.votacoes.models.VotoModel;
import com.api.votacoes.services.interfaces.IAssociadoService;
import com.api.votacoes.services.interfaces.IPautaService;
import com.api.votacoes.services.interfaces.ISessaoService;
import com.api.votacoes.services.interfaces.IVotoService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VotoControllerTest {

    @Mock
    private IVotoService votoService;

    @Mock
    private IPautaService pautaService;

    @Mock
    private ISessaoService sessaoService;

    @Mock
    private IAssociadoService associadoService;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    private VotoModel votoModel;

    private final UUID PAUTA_ID = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new VotoController(votoService, sessaoService, associadoService, pautaService)).build();
        objectMapper = new ObjectMapper();
        votoModel = this.buildVoto();
    }

    @Test
    void testDeveAceitarRequestESalvarUmVoto() throws Exception {
        VotoRequestDto votoRequestDto = new VotoRequestDto("Sim", "775.666.030-78");
        String json = objectMapper.writeValueAsString(votoRequestDto);

        when(pautaService.buscarPorId(PAUTA_ID)).thenReturn(Optional.of(votoModel.getPauta()));
        when(sessaoService.estaEncerrada(PAUTA_ID)).thenReturn(false);
        when(associadoService.buscarPorCpf(votoModel.getAssociado().getCpf())).thenReturn(Optional.of(votoModel.getAssociado()));
        when(votoService.associadoJaVotou(PAUTA_ID, votoModel.getAssociado().getCpf())).thenReturn(false);
        when(votoService.salvar(votoModel)).thenReturn(votoModel);

        String pautaUrlId = PAUTA_URL_ID.replace("{pautaId}", String.valueOf(PAUTA_ID));

        mockMvc.perform(post(pautaUrlId + "/votar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.voto").value(votoModel.getVoto()))
                .andExpect(jsonPath("$.dataCriacao").isNotEmpty());

        verify(votoService, times(1)).salvar(votoModel);
    }

    @Test
    void testDeveAceitarRequestEBuscarOResultadoDaVotacao() throws Exception {
        ResultadoResponseDto resultadoResponseDto = new ResultadoResponseDto(5, 6);

        when(sessaoService.estaEncerrada(PAUTA_ID)).thenReturn(true);
        when(votoService.buscarResultado(PAUTA_ID)).thenReturn(resultadoResponseDto);

        String pautaUrlId = PAUTA_URL_ID.replace("{pautaId}", String.valueOf(PAUTA_ID));

        mockMvc.perform(get(pautaUrlId + "/resultado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aprovada").value(resultadoResponseDto.isAprovada()))
                .andExpect(jsonPath("$.totalVotos").value(resultadoResponseDto.getTotalVotos()))
                .andExpect(jsonPath("$.resultado").value(resultadoResponseDto.getResultado()));

        verify(votoService, times(1)).buscarResultado(PAUTA_ID);
    }

    private VotoModel buildVoto() {
        LocalDate localDate = LocalDate.now(ZoneId.of("UTC"));

        PautaModel pautaModel = new PautaModel();
        pautaModel.setId(PAUTA_ID);
        pautaModel.setTitulo("Pauta Teste 1");
        pautaModel.setDescricao("Pauta para testar o endpoint post do recurso /pauta/id/sessao/iniciar");
        pautaModel.setDataCriacao(localDate);

        AssociadoModel associadoModel = new AssociadoModel();
        associadoModel.setNome("Associado Teste 1");
        associadoModel.setCpf("775.666.030-78");
        associadoModel.setDataCriacao(localDate);

        return VotoModel.build("Sim", pautaModel, associadoModel);
    }
}
