package com.api.votacoes.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@AllArgsConstructor
public class ErroResponseDto {

    @NotNull
    private int codigo;

    @NotBlank
    private String mensagem;

    @NotBlank
    private String erro;

    @NotNull
    private LocalDateTime data;

    public static ErroResponseDto build(int codigo, Exception ex) {
        return new ErroResponseDto(codigo, ex.getMessage(), ex.getClass().getSimpleName(), LocalDateTime.now(ZoneId.of("UTC")));
    }
}
