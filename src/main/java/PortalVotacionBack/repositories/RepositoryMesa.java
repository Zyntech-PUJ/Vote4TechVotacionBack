package PortalVotacionBack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PortalVotacionBack.entities.Mesa;

@Repository
public interface RepositoryMesa extends JpaRepository<Mesa, Long> {

}
