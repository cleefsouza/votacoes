package com.api.votacoes.exceptions;

public class SessaoEncerradaException extends RuntimeException {
    public SessaoEncerradaException(String message) {
        super(message);
    }
}
