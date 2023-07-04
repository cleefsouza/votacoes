package com.api.votacoes.services.interfaces;

import com.api.votacoes.models.AssociadoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IAssociadoService {

    AssociadoModel salvar(AssociadoModel associadoModel);

    Optional<AssociadoModel> buscarPorCpf(String cpf);

    boolean associadoJaExiste(String cpf);

    Page<AssociadoModel> buscarAssociados(Pageable pageable);
}
