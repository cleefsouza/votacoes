package com.api.votacoes.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_SESSAO")
public class SessaoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private int duracao;

    @ManyToOne
    @JoinColumn(name = "PAUTA_ID")
    private PautaModel pauta;

    @Column(nullable = false)
    private boolean encerrada;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    public void encerrarSessao() {
        this.encerrada = true;
    }
}
