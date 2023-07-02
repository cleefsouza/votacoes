package com.api.votacoes.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class VotoRequestDto {

    @NotBlank(message = "Voto com formato inválido.")
    @Size(min = 3)
    private String voto;

    @NotBlank
    @CPF(message = "CPF com formato inválido.")
    private String cpf;
}
