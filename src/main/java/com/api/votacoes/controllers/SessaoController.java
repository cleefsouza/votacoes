package com.api.votacoes.controllers;

import com.api.votacoes.dtos.request.SessaoRequestDto;
import com.api.votacoes.models.SessaoModel;
import com.api.votacoes.services.interfaces.IPautaService;
import com.api.votacoes.services.interfaces.ISessaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static com.api.votacoes.utils.ConstantesUtils.PAUTA_URL_ID;
import static com.api.votacoes.utils.ConstantesUtils.SESSAO_URL;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(PAUTA_URL_ID + SESSAO_URL)
public class SessaoController {

    private final ISessaoService sessaoService;

    private final IPautaService pautaService;

    public SessaoController(ISessaoService sessaoService, IPautaService pautaService) {
        this.sessaoService = sessaoService;
        this.pautaService = pautaService;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<Object> salvar(@PathVariable @Valid UUID pautaId, @RequestBody @Valid SessaoRequestDto sessaoRequestDto) {

        if (sessaoService.existeSessao(pautaId)) {
            throw new DataIntegrityViolationException("Já existe uma sessão para essa pauta.");
        }

        var pauta = pautaService.buscarPorId(pautaId);

        if (!pauta.isPresent()) {
            throw new EntityNotFoundException("Pauta não encontrada.");
        }

        SessaoModel sessaoModel = new SessaoModel();
        BeanUtils.copyProperties(sessaoRequestDto, sessaoModel);
        sessaoModel.setPauta(pauta.get());
        sessaoModel.setDataCriacao(LocalDateTime.now(ZoneId.of("UTC")));

        SessaoModel response = sessaoService.salvar(sessaoModel);
        sessaoService.iniciarSessao(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
