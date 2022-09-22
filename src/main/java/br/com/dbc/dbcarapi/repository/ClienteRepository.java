package br.com.dbc.dbcarapi.repository;


import br.com.dbc.dbcarapi.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {
    Optional<ClienteEntity> findByIdUsuario(Integer idUsuario);
}
