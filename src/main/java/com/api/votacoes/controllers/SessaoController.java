package com.api.votacoes.controllers;

import com.api.votacoes.dtos.request.SessaoRequestDto;
import com.api.votacoes.models.SessaoModel;
import com.api.votacoes.services.interfaces.IPautaService;
import com.api.votacoes.services.interfaces.ISessaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static com.api.votacoes.utils.ConstantesUtils.PAUTA_URL;
import static com.api.votacoes.utils.ConstantesUtils.SESSAO_URL;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(PAUTA_URL + "/{pautaId}" + SESSAO_URL)
public class SessaoController {

    final ISessaoService sessaoService;

    final IPautaService pautaService;

    public SessaoController(ISessaoService sessaoService, IPautaService pautaService) {
        this.sessaoService = sessaoService;
        this.pautaService = pautaService;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<Object> salvar(@PathVariable @Valid UUID pautaId, @RequestBody @Valid SessaoRequestDto sessaoRequestDto) {

        var pauta = pautaService.buscarPorId(pautaId);

        if (!pauta.isPresent()) {
            throw new EntityNotFoundException("Pauta não encontrada.");
        }

        var sessaoModel = new SessaoModel();
        BeanUtils.copyProperties(sessaoRequestDto, sessaoModel);
        sessaoModel.setPauta(pauta.get());
        sessaoModel.setDataCriacao(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(sessaoService.salvar(sessaoModel));
    }
}
