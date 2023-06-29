package com.api.votacoes.services;

import com.api.votacoes.models.SessaoModel;
import com.api.votacoes.repositories.SessaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SessaoService implements ISessaoService {

    final SessaoRepository sessaoRepository;

    public SessaoService(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    @Transactional
    public SessaoModel salvar(SessaoModel sessaoModel) {
        return sessaoRepository.save(sessaoModel);
    }

    public Page<SessaoModel> buscarSessoes(Pageable pageable) {
        return sessaoRepository.findAll(pageable);
    }
}
