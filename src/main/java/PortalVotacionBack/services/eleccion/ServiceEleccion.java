package PortalVotacionBack.services.eleccion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import PortalVotacionBack.dtos.candidato.MapperCandidato;
import PortalVotacionBack.dtos.candidato.ResponseCandidatoDTO;
import PortalVotacionBack.dtos.eleccion.MapperEleccion;
import PortalVotacionBack.dtos.eleccion.ResponseEleccionDTO;
import PortalVotacionBack.entities.Eleccion;
import PortalVotacionBack.enums.EstadoEleccion;
import PortalVotacionBack.exceptions.ResourceNotFoundException;
import PortalVotacionBack.repositories.RepositoryCandidato;
import PortalVotacionBack.repositories.RepositoryEleccion;

@Service
public class ServiceEleccion implements IServiceEleccion {

  @Autowired
  RepositoryEleccion repositoryEleccion;

  @Autowired
  RepositoryCandidato repositoryCandidato;

  @Autowired
  MapperEleccion mapperEleccion;

  @Autowired
  MapperCandidato mapperCandidato;

  @Override
  public List<ResponseEleccionDTO> findActivas() {
    return mapperEleccion.toResponseDTOs(
        repositoryEleccion.findByEstado(EstadoEleccion.EN_CURSO));
  }

  @Override
  public ResponseEleccionDTO findById(Long id) {
    Eleccion eleccion = repositoryEleccion.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Elección no encontrada con id: " + id));

    List<ResponseCandidatoDTO> candidatos = mapperCandidato.toResponseDTOs(
        repositoryCandidato.findByLista_Eleccion_IdEleccionAndActivoTrue(id));

    return mapperEleccion.toResponseDTO(eleccion, candidatos);
  }

  @Override
  public List<ResponseCandidatoDTO> findCandidatosByEleccion(Long idEleccion) {
    if (!repositoryEleccion.existsById(idEleccion))
      throw new ResourceNotFoundException("Elección no encontrada con id: " + idEleccion);

    return mapperCandidato.toResponseDTOs(
        repositoryCandidato.findByLista_Eleccion_IdEleccionAndActivoTrue(idEleccion));
  }

}
