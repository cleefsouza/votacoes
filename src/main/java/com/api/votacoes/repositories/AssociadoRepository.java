package com.api.votacoes.repositories;

import com.api.votacoes.models.AssociadoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssociadoRepository extends JpaRepository<AssociadoModel, UUID> {

    Optional<AssociadoModel> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}