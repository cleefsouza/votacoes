package com.api.votacoes.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssociadoRequestDto {

    @NotBlank
    private String nome;

    @NotBlank
    @CPF(message = "CPF com formato inválido")
    private String cpf;
}
