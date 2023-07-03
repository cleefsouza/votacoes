package com.api.votacoes.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Slf4j
@Getter
@Setter
public class ResultadoResponseDto {

    @NotNull
    @JsonIgnore
    private int votosSim;

    @NotNull
    @JsonIgnore
    private int votosNao;

    @NotNull
    private boolean aprovada = false;

    @NotNull
    private int totalVotos;

    @NotBlank
    private String resultado;

    public ResultadoResponseDto(int votosSim, int votosNao) {
        this.votosSim = votosSim;
        this.votosNao = votosNao;
    }

    public void montarResultado() {
        this.totalVotos = this.votosSim + this.votosNao;

        if (this.votosSim > this.votosNao) {
            this.resultado = String.format("Pauta aprovada com %s dos votos a favor", this.votosSim);
            this.aprovada = true;
            return;
        }

        if (votosSim == votosNao) {
            this.resultado = "Votação empatada";
            return;
        }

        this.resultado = String.format("Pauta reprovada com %s dos votos contra", this.votosNao);
    }
}
