package com.api.votacoes.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PautaRequestDto {

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;
}
