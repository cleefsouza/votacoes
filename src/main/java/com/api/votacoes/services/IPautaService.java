package com.api.votacoes.services;

import com.api.votacoes.models.PautaModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface IPautaService {

    PautaModel salvar(PautaModel pautaModel);

    boolean pautaJaExiste(String titulo);

    Page<PautaModel> buscarPautas(Pageable pageable);

    Optional<PautaModel> buscarPorId(UUID id);
}
