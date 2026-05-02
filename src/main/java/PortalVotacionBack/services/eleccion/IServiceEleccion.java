package PortalVotacionBack.services.eleccion;

import java.util.List;

import PortalVotacionBack.dtos.candidato.ResponseCandidatoDTO;
import PortalVotacionBack.dtos.eleccion.ResponseEleccionDTO;

public interface IServiceEleccion {

  List<ResponseEleccionDTO> findActivas();

  ResponseEleccionDTO findById(Long id);

  List<ResponseCandidatoDTO> findCandidatosByEleccion(Long idEleccion);

}
