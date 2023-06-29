package com.api.votacoes.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class SessaoDto {

    @Min(1)
    private int duracao = 1;

    @NotNull
    private UUID pautaId;

    @NotNull
    private boolean encerrada = false;
}
