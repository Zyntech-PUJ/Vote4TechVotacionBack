package PortalVotacionBack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PortalVotacionBack.entities.CentroVotacion;

@Repository
public interface RepositoryCentroVotacion extends JpaRepository<CentroVotacion, Long> {

}
