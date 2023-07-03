package com.api.votacoes.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_VOTO")
public class VotoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 3)
    private String voto;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAUTA_ID", nullable = false)
    private PautaModel pauta;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSOCIADO_ID", nullable = false)
    private AssociadoModel associado;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    public static VotoModel build(String voto, PautaModel pauta, AssociadoModel associado) {

        var votoModel = new VotoModel();
        votoModel.setVoto(voto);
        votoModel.setPauta(pauta);
        votoModel.setAssociado(associado);
        votoModel.setDataCriacao(LocalDateTime.now(ZoneId.of("UTC")));

        return votoModel;
    }
}
