package com.api.votacoes.dtos.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SessaoRequestDto {

    @Min(1)
    private int duracao = 1;

    @NotNull
    private boolean encerrada = false;
}
