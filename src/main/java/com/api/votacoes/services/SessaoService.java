package com.api.votacoes.services;

import com.api.votacoes.dtos.response.ResultadoResponseDto;
import com.api.votacoes.models.SessaoModel;
import com.api.votacoes.repositories.SessaoRepository;
import com.api.votacoes.services.interfaces.ISessaoService;
import com.api.votacoes.services.interfaces.IVotoService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SessaoService implements ISessaoService {

    private final SessaoRepository sessaoRepository;

    private final IVotoService votoService;

    private final RabbitMqService rabbitMqService;

    private ScheduledExecutorService executorService;

    public SessaoService(SessaoRepository sessaoRepository, IVotoService votoService, RabbitMqService rabbitMqService) {
        this.sessaoRepository = sessaoRepository;
        this.votoService = votoService;
        this.rabbitMqService = rabbitMqService;
    }

    @Transactional
    public SessaoModel salvar(SessaoModel sessaoModel) {
        return sessaoRepository.save(sessaoModel);
    }

    public boolean estaEncerrada(UUID pautaId) {

        if (!sessaoRepository.existsByPauta_Id(pautaId)) {
            log.error(String.format("Não existe sessão de votação para pauta %s no banco de dados", pautaId));
            throw new EntityNotFoundException("Sessão não encontrada");
        }

        return sessaoRepository.estaEncerrada(pautaId);
    }

    public void iniciarSessao(SessaoModel sessao) {
        try {
            log.info(String.format("Iniciando sessão de votação %s com duração de %d minutos", sessao.getId(), sessao.getDuracao()));
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(() -> this.encerrarSessao(sessao), sessao.getDuracao(), TimeUnit.MINUTES);
        } catch (Exception ex) {
            log.error("Ocorreu um erro ao tentar iniciar uma sessão de votação");
            throw new IllegalArgumentException("Não foi possivel iniciar a sessão");
        }
    }

    public void encerrarSessao(SessaoModel sessao) {
        log.info(String.format("Encerrando sessão de votação %s com duração de %d minutos", sessao.getId(), sessao.getDuracao()));

        sessao.encerrarSessao();
        sessaoRepository.save(sessao);

        log.info("Buscando resultado da votação");
        ResultadoResponseDto resultado = votoService.buscarResultado(sessao.getPauta().getId());

        log.info("Iniciando envio de mensagem para fila no rabbitmq");
        rabbitMqService.enviarMensagem(resultado);

        executorService.shutdown();
        log.info(String.format("Sessão %s encerrada com sucesso", sessao.getId()));
    }

    public boolean existeSessao(UUID pautaId) {
        return sessaoRepository.existsByPauta_Id(pautaId);
    }

    protected void setExecutorService(ScheduledExecutorService scheduledExecutorService) {
        this.executorService = scheduledExecutorService;
    }
}
