package PortalVotacionBack.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PortalVotacionBack.entities.Eleccion;
import PortalVotacionBack.enums.EstadoEleccion;

@Repository
public interface RepositoryEleccion extends JpaRepository<Eleccion, Long> {

  List<Eleccion> findByEstado(EstadoEleccion estado);

}
