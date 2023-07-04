package com.api.votacoes.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessaoRequestDto {

    @Min(1)
    private int duracao = 1;

    @NotNull
    private boolean encerrada = false;
}
