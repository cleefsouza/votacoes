package com.api.votacoes.repositories;

import com.api.votacoes.models.SessaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SessaoRepository  extends JpaRepository<SessaoModel, UUID> {
}
