package PortalVotacionBack.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PortalVotacionBack.entities.YaVoto;

@Repository
public interface RepositoryYaVoto extends JpaRepository<YaVoto, Long> {

  boolean existsByCedulaAndIdEleccion(String cedula, Long idEleccion);

  Optional<YaVoto> findByCedulaAndIdEleccion(String cedula, Long idEleccion);

}
