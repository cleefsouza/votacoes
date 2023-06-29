package com.api.votacoes.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PautaDto {

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;
}
