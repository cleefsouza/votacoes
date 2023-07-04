package com.api.votacoes.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VotoRequestDto {

    @NotBlank(message = "Voto com formato inválido")
    @Size(min = 3)
    private String voto;

    @NotBlank
    @CPF(message = "CPF com formato inválido")
    private String cpf;

    public void validarVoto() {
        if (!this.voto.equalsIgnoreCase("Sim") && !this.voto.equalsIgnoreCase("Não")) {
            log.error(String.format("Voto \"%s\" do associado \"%s\" com formato inválido", this.voto, this.cpf));
            throw new IllegalArgumentException("Voto com formato inválido");
        }
    }
}
