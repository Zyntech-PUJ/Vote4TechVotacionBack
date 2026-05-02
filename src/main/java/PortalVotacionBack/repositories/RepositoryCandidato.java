package PortalVotacionBack.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PortalVotacionBack.entities.Candidato;

@Repository
public interface RepositoryCandidato extends JpaRepository<Candidato, Long> {

  List<Candidato> findByLista_Eleccion_IdEleccionAndActivoTrue(Long idEleccion);

}
