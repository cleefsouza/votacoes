package com.api.votacoes.services.interfaces;

import com.api.votacoes.models.AssociadoModel;

import java.util.Optional;

public interface IAssociadoService {

    AssociadoModel salvar(AssociadoModel associadoModel);

    Optional<AssociadoModel> buscarPorCpf(String cpf);

    boolean associadoJaExiste(String cpf);
}
