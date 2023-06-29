package com.api.votacoes.services;

import com.api.votacoes.models.PautaModel;
import com.api.votacoes.repositories.PautaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class PautaService implements IPautaService {

    final PautaRepository pautaRepository;

    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    @Transactional
    public PautaModel salvar(PautaModel pautaModel) {
        return pautaRepository.save(pautaModel);
    }

    public boolean pautaJaExiste(String titulo) {
        return pautaRepository.existsByTitulo(titulo);
    }

    public Page<PautaModel> buscarPautas(Pageable pageable) {
        return pautaRepository.findAll(pageable);
    }

    public Optional<PautaModel> buscarPorId(UUID id) {
        return pautaRepository.findById(id);
    }
}
