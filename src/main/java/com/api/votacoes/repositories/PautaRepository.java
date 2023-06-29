package com.api.votacoes.repositories;

import com.api.votacoes.models.PautaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PautaRepository extends JpaRepository<PautaModel, UUID> {

    boolean existsByTitulo(String titulo);
}
