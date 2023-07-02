package com.api.votacoes.dtos.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PautaRequestDto {

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;
}
