package com.api.votacoes.repositories;

import com.api.votacoes.models.VotoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VotoRepository extends JpaRepository<VotoModel, UUID> {

    boolean existsByPauta_IdAndAssociado_Cpf(UUID pautaId, String cpf);

    @Query(value = "SELECT COUNT(CASE WHEN v.voto = 'Sim' THEN 1 END) AS votosSim, COUNT(CASE WHEN v.voto = 'Não' THEN 1 END) AS votosNao FROM VotoModel v WHERE v.pauta.id = :idPauta")
    List<Object[]> buscarVotos(UUID idPauta);
}
