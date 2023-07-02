package com.api.votacoes.services;

import com.api.votacoes.dtos.response.ResultadoResponseDto;
import com.api.votacoes.models.VotoModel;
import com.api.votacoes.repositories.PautaRepository;
import com.api.votacoes.repositories.SessaoRepository;
import com.api.votacoes.repositories.VotoRepository;
import com.api.votacoes.services.interfaces.IVotoService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class VotoService implements IVotoService {

    private final VotoRepository votoRepository;

    private final PautaRepository pautaRepository;

    private final SessaoRepository sessaoRepository;

    public VotoService(VotoRepository votoRepository, PautaRepository pautaRepository, SessaoRepository sessaoRepository) {
        this.votoRepository = votoRepository;
        this.pautaRepository = pautaRepository;
        this.sessaoRepository = sessaoRepository;
    }

    @Transactional
    public VotoModel salvar(VotoModel votoModel) {
        return votoRepository.save(votoModel);
    }

    public boolean associadoJaVotou(UUID pautaId, String cpf) {
        return votoRepository.existsByPauta_IdAndAssociado_Cpf(pautaId, cpf);
    }

    public ResultadoResponseDto buscarResultado(UUID pautaId) {

        if (!pautaRepository.existsById(pautaId)) {
            throw new EntityNotFoundException("Pauta não encontrada.");
        }

        if (!sessaoRepository.existsByPauta_Id(pautaId)) {
            throw new EntityNotFoundException("Não a sessão de votação para essa pauta.");
        }

        var votos = votoRepository.buscarVotos(pautaId).get(0);

        ResultadoResponseDto resultado = new ResultadoResponseDto(Integer.parseInt(votos[0].toString()), Integer.parseInt(votos[1].toString()));
        resultado.montarResultado();

        return resultado;
    }
}
