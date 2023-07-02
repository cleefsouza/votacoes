package com.api.votacoes.controllers;

import com.api.votacoes.dtos.request.AssociadoRequestDto;
import com.api.votacoes.models.AssociadoModel;
import com.api.votacoes.services.interfaces.IAssociadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.api.votacoes.utils.ConstantesUtils.ASSOCIADO_URL;

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
            throw new DataIntegrityViolationException("Já existe um associado com esse CPF.");
        }

        var associadoModel = new AssociadoModel();
        BeanUtils.copyProperties(associadoRequestDto, associadoModel);
        associadoModel.setDataCriacao(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(associadoService.salvar(associadoModel));
    }
}
