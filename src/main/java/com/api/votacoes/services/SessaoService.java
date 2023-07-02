package com.api.votacoes.services;

import com.api.votacoes.models.SessaoModel;
import com.api.votacoes.repositories.SessaoRepository;
import com.api.votacoes.services.interfaces.ISessaoService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class SessaoService implements ISessaoService {

    final SessaoRepository sessaoRepository;

    public SessaoService(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    @Transactional
    public SessaoModel salvar(SessaoModel sessaoModel) {
        // criar job para encerrar sessão

        return sessaoRepository.save(sessaoModel);
    }

    public boolean estaEncerrada(UUID pautaId) {

        if (!sessaoRepository.existsByPauta_Id(pautaId)) {
            throw new EntityNotFoundException("Sessão não encontrada.");
        }

        return sessaoRepository.estaEncerrada(pautaId);
    }
}
