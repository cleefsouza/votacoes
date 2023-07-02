package com.api.votacoes.services;

import com.api.votacoes.models.SessaoModel;
import com.api.votacoes.repositories.SessaoRepository;
import com.api.votacoes.services.interfaces.ISessaoService;
import com.api.votacoes.services.interfaces.IVotoService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SessaoService implements ISessaoService {

    private final SessaoRepository sessaoRepository;

    private final IVotoService votoService;

    private ScheduledExecutorService executorService;

    public SessaoService(SessaoRepository sessaoRepository, IVotoService votoService) {
        this.sessaoRepository = sessaoRepository;
        this.votoService = votoService;
    }

    @Transactional
    public SessaoModel salvar(SessaoModel sessaoModel) {
        return sessaoRepository.save(sessaoModel);
    }

    public boolean estaEncerrada(UUID pautaId) {

        if (!sessaoRepository.existsByPauta_Id(pautaId)) {
            throw new EntityNotFoundException("Sessão não encontrada.");
        }

        return sessaoRepository.estaEncerrada(pautaId);
    }

    public void iniciarSessao(SessaoModel sessao) {
        try {
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(() -> this.encerrarSessao(sessao), sessao.getDuracao(), TimeUnit.MINUTES);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Não foi possivel iniciar a sessão.");
        }
    }

    public void encerrarSessao(SessaoModel sessao) {
        sessao.encerrarSessao();
        sessaoRepository.save(sessao);

        votoService.buscarResultado(sessao.getPauta().getId());

        executorService.shutdown();
    }
}
