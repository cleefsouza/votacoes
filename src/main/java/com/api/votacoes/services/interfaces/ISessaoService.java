package com.api.votacoes.services.interfaces;

import com.api.votacoes.models.SessaoModel;

import java.util.UUID;

public interface ISessaoService {

    SessaoModel salvar(SessaoModel sessaoModel);

    boolean estaEncerrada(UUID pautaId);

    void iniciarSessao(SessaoModel sessao);

    boolean existeSessao(UUID pautaId);
}
