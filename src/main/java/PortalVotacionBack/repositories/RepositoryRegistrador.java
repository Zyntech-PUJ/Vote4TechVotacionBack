package PortalVotacionBack.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PortalVotacionBack.entities.Registrador;

@Repository
public interface RepositoryRegistrador extends JpaRepository<Registrador, Long> {

  Optional<Registrador> findByUsuario(String usuario);

}
