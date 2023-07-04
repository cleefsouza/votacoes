package com.api.votacoes.controllers;

import com.api.votacoes.dtos.request.VotoRequestDto;
import com.api.votacoes.dtos.response.ResultadoResponseDto;
import com.api.votacoes.exceptions.AssociadoJaVotouException;
import com.api.votacoes.exceptions.SessaoEncerradaException;
import com.api.votacoes.models.AssociadoModel;
import com.api.votacoes.models.PautaModel;
import com.api.votacoes.models.VotoModel;
import com.api.votacoes.services.interfaces.IAssociadoService;
import com.api.votacoes.services.interfaces.IPautaService;
import com.api.votacoes.services.interfaces.ISessaoService;
import com.api.votacoes.services.interfaces.IVotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static com.api.votacoes.utils.ConstantesUtils.PAUTA_URL_ID;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(PAUTA_URL_ID)
public class VotoController {

    private final IVotoService votoService;

    private final ISessaoService sessaoService;

    private final IAssociadoService associadoService;

    private final IPautaService pautaService;

    public VotoController(IVotoService votoService, ISessaoService sessaoService, IAssociadoService associadoService, IPautaService pautaService) {
        this.votoService = votoService;
        this.sessaoService = sessaoService;
        this.associadoService = associadoService;
        this.pautaService = pautaService;
    }

    @PostMapping("/votar")
    public ResponseEntity<Object> salvar(@PathVariable @Valid UUID pautaId, @RequestBody @Valid VotoRequestDto votoRequestDto) {

        votoRequestDto.validarVoto();

        PautaModel pauta = this.validarPauta(pautaId);

        AssociadoModel associado = this.validarAssociado(votoRequestDto.getCpf(), pauta.getId());

        var votoModel = VotoModel.build(votoRequestDto.getVoto(), pauta, associado);

        log.info("Iniciando persistencia do voto na pauta " + pautaId);
        return ResponseEntity.status(HttpStatus.OK).body(votoService.salvar(votoModel));
    }

    @GetMapping("/resultado")
    public ResponseEntity<ResultadoResponseDto> buscarResultado(@PathVariable @Valid UUID pautaId) {

        if (!sessaoService.estaEncerrada(pautaId)) {
            log.error(String.format("Não foi possível gerar o resultado, votação da pauta %s em andamento", pautaId));
            throw new DataIntegrityViolationException("Sessão em andamento");
        }

        log.info("Buscando resultado da votação na pauta " + pautaId);
        return ResponseEntity.status(HttpStatus.OK).body(votoService.buscarResultado(pautaId));
    }

    private PautaModel validarPauta(UUID pautaId) {

        Optional<PautaModel> pauta = pautaService.buscarPorId(pautaId);

        if (!pauta.isPresent()) {
            log.error(String.format("Pauta %s não encontrada no banco de dados", pautaId));
            throw new EntityNotFoundException("Pauta não encontrada");
        }

        if (sessaoService.estaEncerrada(pauta.get().getId())) {
            log.error("Votação encerrada para pauta " + pauta.get().getId());
            throw new SessaoEncerradaException("Sessão encerrada");
        }

        return pauta.get();
    }

    private AssociadoModel validarAssociado(String cpf, UUID pautaId) {

        Optional<AssociadoModel> associado = associadoService.buscarPorCpf(cpf);

        if (!associado.isPresent()) {
            log.error(String.format("Associado %s não encontrada no banco de dados", cpf));
            throw new EntityNotFoundException("Associado não encontrado");
        }

        if (votoService.associadoJaVotou(pautaId, associado.get().getCpf())) {
            log.error(String.format("Associado %s já votou na pauta %s", associado.get().getCpf(), pautaId));
            throw new AssociadoJaVotouException("Associado já votou nessa pauta");
        }

        return associado.get();
    }
}
