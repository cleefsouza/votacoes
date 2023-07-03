package com.api.votacoes.exceptions;

public class MensagemNaoEnviadaException extends RuntimeException {
    public MensagemNaoEnviadaException(String message) {
        super(message);
    }
}
