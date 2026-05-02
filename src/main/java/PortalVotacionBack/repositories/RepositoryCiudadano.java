package PortalVotacionBack.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PortalVotacionBack.entities.Ciudadano;

@Repository
public interface RepositoryCiudadano extends JpaRepository<Ciudadano, Long> {

  Optional<Ciudadano> findByCedula(String cedula);

}
