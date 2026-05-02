package PortalVotacionBack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PortalVotacionBack.entities.VotoSql;

@Repository
public interface RepositoryVotoSql extends JpaRepository<VotoSql, String> {
}
