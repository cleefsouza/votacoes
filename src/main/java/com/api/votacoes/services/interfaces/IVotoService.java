package com.api.votacoes.services.interfaces;

import com.api.votacoes.dtos.response.ResultadoResponseDto;
import com.api.votacoes.models.VotoModel;

import java.util.UUID;

public interface IVotoService {

    VotoModel salvar(VotoModel votoModel);

    boolean associadoJaVotou(UUID pautaId, String cpf);

    ResultadoResponseDto buscarResultado(UUID pautaId);
}
