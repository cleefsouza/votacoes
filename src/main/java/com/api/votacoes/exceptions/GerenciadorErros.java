package com.api.votacoes.exceptions;

import com.api.votacoes.dtos.response.ErroResponseDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GerenciadorErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> recursoNaoEncontrado(Exception ex) {
        return new ResponseEntity<>(ErroResponseDto.build(HttpStatus.NOT_FOUND.value(), ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> recursoComConflito(Exception ex) {
        return new ResponseEntity<>(ErroResponseDto.build(HttpStatus.CONFLICT.value(), ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AssociadoJaVotouException.class)
    public ResponseEntity<Object> associadoJaVotou(Exception ex) {
        return new ResponseEntity<>(ErroResponseDto.build(HttpStatus.FORBIDDEN.value(), ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SessaoEncerradaException.class)
    public ResponseEntity<Object> sessaoEncerrada(Exception ex) {
        return new ResponseEntity<>(ErroResponseDto.build(HttpStatus.BAD_REQUEST.value(), ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Object> argumentoInvalido(Exception ex) {
        return new ResponseEntity<>(ErroResponseDto.build(HttpStatus.BAD_REQUEST.value(), ex), HttpStatus.BAD_REQUEST);
    }
}
