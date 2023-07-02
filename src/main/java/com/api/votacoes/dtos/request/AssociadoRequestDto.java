package com.api.votacoes.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AssociadoRequestDto {

    @NotBlank
    private String nome;

    @NotBlank
    @CPF(message = "CPF com formato inválido.")
    private String cpf;
}
