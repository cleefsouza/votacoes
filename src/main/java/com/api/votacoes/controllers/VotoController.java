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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static com.api.votacoes.utils.ConstantesUtils.PAUTA_URL;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(PAUTA_URL + "/{pautaId}")
public class VotoController {

    final IVotoService votoService;

    final ISessaoService sessaoService;

    final IAssociadoService associadoService;

    final IPautaService pautaService;

    public VotoController(IVotoService votoService, ISessaoService sessaoService, IAssociadoService associadoService, IPautaService pautaService) {
        this.votoService = votoService;
        this.sessaoService = sessaoService;
        this.associadoService = associadoService;
        this.pautaService = pautaService;
    }

    @PostMapping("/votar")
    public ResponseEntity<Object> salvar(@PathVariable @Valid UUID pautaId, @RequestBody @Valid VotoRequestDto votoRequestDto) {

        Optional<PautaModel> pauta = pautaService.buscarPorId(pautaId);
        this.validarPauta(pauta);

        // verificar cpf do associado

        var associado = associadoService.buscarPorCpf(votoRequestDto.getCpf());
        this.validarAssociado(associado, pauta.get().getId());

        var votoModel = VotoModel.build(votoRequestDto.getVoto(), pauta.get(), associado.get());

        return ResponseEntity.status(HttpStatus.OK).body(votoService.salvar(votoModel));
    }

    @GetMapping("/resultado")
    public ResponseEntity<ResultadoResponseDto> buscarResultado(@PathVariable @Valid UUID pautaId) {
        return ResponseEntity.status(HttpStatus.OK).body(votoService.buscarResultado(pautaId));
    }

    private void validarPauta(Optional<PautaModel> pauta) {

        if (!pauta.isPresent()) {
            throw new EntityNotFoundException("Pauta não encontrada.");
        }

        if (sessaoService.estaEncerrada(pauta.get().getId())) {
            throw new SessaoEncerradaException("Sessão encerrada.");
        }
    }

    private void validarAssociado(Optional<AssociadoModel> associado, UUID pautaId) {

        if (!associado.isPresent()) {
            throw new EntityNotFoundException("Associado não encontrado.");
        }

        if (votoService.associadoJaVotou(pautaId, associado.get().getCpf())) {
            throw new AssociadoJaVotouException("Associado já votou nessa pauta.");
        }
    }
}
