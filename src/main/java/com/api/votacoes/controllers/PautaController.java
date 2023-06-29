package com.api.votacoes.controllers;

import com.api.votacoes.dtos.PautaDto;
import com.api.votacoes.models.PautaModel;
import com.api.votacoes.services.IPautaService;
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
@RequestMapping("/pauta")
public class PautaController {

    final IPautaService pautaService;

    public PautaController(IPautaService pautaService) {
        this.pautaService = pautaService;
    }

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid PautaDto pautaDto) {

        if (pautaService.pautaJaExiste(pautaDto.getTitulo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: Já existe uma pauta com esse título em uso.");
        }

        var pautaModel = new PautaModel();
        BeanUtils.copyProperties(pautaDto, pautaModel);
        pautaModel.setDataCriacao(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(pautaService.salvar(pautaModel));
    }

    @GetMapping
    public ResponseEntity<Page<PautaModel>> buscarPautas(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.buscarPautas(pageable));
    }
}
