package com.api.votacoes.controllers;

import com.api.votacoes.dtos.SessaoDto;
import com.api.votacoes.models.SessaoModel;
import com.api.votacoes.services.IPautaService;
import com.api.votacoes.services.ISessaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/sessao")
public class SessaoController {

    final ISessaoService sessaoService;

    final IPautaService pautaService;

    public SessaoController(ISessaoService sessaoService, IPautaService pautaService) {
        this.sessaoService = sessaoService;
        this.pautaService = pautaService;
    }

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid SessaoDto sessaoDto) {

        var pauta = pautaService.buscarPorId(sessaoDto.getPautaId());

        if (!pauta.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pauta não encontrada.");
        }

        var sessaoModel = new SessaoModel();
        BeanUtils.copyProperties(sessaoDto, sessaoModel);
        sessaoModel.setPauta(pauta.get());
        sessaoModel.setDataCriacao(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(sessaoService.salvar(sessaoModel));
    }

    @GetMapping
    public ResponseEntity<Page<SessaoModel>> buscarSessoes(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(sessaoService.buscarSessoes(pageable));
    }
}
