package com.api.votacoes.repositories;

import com.api.votacoes.models.SessaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SessaoRepository extends JpaRepository<SessaoModel, UUID> {

    @Query(value = "SELECT s.encerrada FROM SessaoModel s WHERE s.pauta.id = :pautaId")
    boolean estaEncerrada(UUID pautaId);

    boolean existsByPauta_Id(UUID pautaId);
}
