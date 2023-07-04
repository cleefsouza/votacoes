package com.api.votacoes.controllers;

import com.api.votacoes.dtos.request.AssociadoRequestDto;
import com.api.votacoes.models.AssociadoModel;
import com.api.votacoes.services.interfaces.IAssociadoService;
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

import static com.api.votacoes.utils.ConstantesUtils.ASSOCIADO_URL;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(ASSOCIADO_URL)
public class AssociadoController {

    private final IAssociadoService associadoService;

    public AssociadoController(IAssociadoService associadoService) {
        this.associadoService = associadoService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Object> salvar(@RequestBody @Valid AssociadoRequestDto associadoRequestDto) {

        if (associadoService.associadoJaExiste(associadoRequestDto.getCpf())) {
            log.error(String.format("Associado com CPF \"%s\" já existe no banco de dados", associadoRequestDto.getCpf()));
            throw new DataIntegrityViolationException("Já existe um associado com esse CPF");
        }

        var associadoModel = new AssociadoModel();
        BeanUtils.copyProperties(associadoRequestDto, associadoModel);
        associadoModel.setDataCriacao(LocalDate.now(ZoneId.of("UTC")));

        log.info(String.format("Iniciando persistencia do associado \"%s\"", associadoModel.getNome()));
        return ResponseEntity.status(HttpStatus.CREATED).body(associadoService.salvar(associadoModel));
    }

    @GetMapping
    public ResponseEntity<Page<AssociadoModel>> buscarAssociados(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Listando associados cadastrados");
        return ResponseEntity.status(HttpStatus.OK).body(associadoService.buscarAssociados(pageable));
    }
}
