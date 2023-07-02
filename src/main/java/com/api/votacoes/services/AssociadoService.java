package com.api.votacoes.services;

import com.api.votacoes.models.AssociadoModel;
import com.api.votacoes.repositories.AssociadoRepository;
import com.api.votacoes.services.interfaces.IAssociadoService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AssociadoService implements IAssociadoService {

    final AssociadoRepository associadoRepository;

    public AssociadoService(AssociadoRepository associadoRepository) {
        this.associadoRepository = associadoRepository;
    }

    @Transactional
    public AssociadoModel salvar(AssociadoModel associadoModel) {
        return associadoRepository.save(associadoModel);
    }

    public Optional<AssociadoModel> buscarPorCpf(String cpf) {
        return associadoRepository.findByCpf(cpf);
    }

    public boolean associadoJaExiste(String cpf) {
        return associadoRepository.existsByCpf(cpf);
    }
}
