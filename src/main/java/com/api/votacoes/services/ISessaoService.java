package com.api.votacoes.services;

import com.api.votacoes.models.SessaoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISessaoService {

    SessaoModel salvar(SessaoModel sessaoModel);

    Page<SessaoModel> buscarSessoes(Pageable pageable);
}
