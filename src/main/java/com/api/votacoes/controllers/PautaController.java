package com.api.votacoes.controllers;

import com.api.votacoes.dtos.request.PautaRequestDto;
import com.api.votacoes.models.PautaModel;
import com.api.votacoes.services.interfaces.IPautaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;

import static com.api.votacoes.utils.ConstantesUtils.PAUTA_URL;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(PAUTA_URL)
public class PautaController {

    private final IPautaService pautaService;

    public PautaController(IPautaService pautaService) {
        this.pautaService = pautaService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Object> salvar(@RequestBody @Valid PautaRequestDto pautaRequestDto) {

        if (pautaService.pautaJaExiste(pautaRequestDto.getTitulo())) {
            log.error(String.format("Pauta com titulo \"%s\" já existe no banco de dados", pautaRequestDto.getTitulo()));
            throw new DataIntegrityViolationException("Já existe uma pauta com esse título em uso");
        }

        var pautaModel = new PautaModel();
        BeanUtils.copyProperties(pautaRequestDto, pautaModel);
        pautaModel.setDataCriacao(LocalDate.now(ZoneId.of("UTC")));

        log.info(String.format("Iniciando persistencia da pauta \"%s\"", pautaModel.getTitulo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(pautaService.salvar(pautaModel));
    }

    @GetMapping
    public ResponseEntity<Page<PautaModel>> buscarPautas(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Listando pautas cadastradas");
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.buscarPautas(pageable));
    }
}
