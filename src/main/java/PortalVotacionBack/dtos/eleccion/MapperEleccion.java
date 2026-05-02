package PortalVotacionBack.dtos.eleccion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import PortalVotacionBack.dtos.candidato.MapperCandidato;
import PortalVotacionBack.dtos.candidato.ResponseCandidatoDTO;
import PortalVotacionBack.entities.Eleccion;

@Component
public class MapperEleccion {

  @Autowired
  MapperCandidato mapperCandidato;

  public ResponseEleccionDTO toResponseDTO(Eleccion eleccion) {
    return toResponseDTO(eleccion, null);
  }

  public ResponseEleccionDTO toResponseDTO(Eleccion eleccion, List<ResponseCandidatoDTO> candidatos) {
    if (eleccion == null) return null;

    ResponseEleccionDTO dto = new ResponseEleccionDTO();
    dto.setIdEleccion(eleccion.getIdEleccion());
    dto.setNombre(eleccion.getNombre());
    dto.setFechaInicio(eleccion.getFechaInicio());
    dto.setFechaFinalizacion(eleccion.getFechaFinalizacion());
    dto.setTipo(eleccion.getTipo() != null ? eleccion.getTipo().name() : null);
    dto.setListaAbierta(eleccion.getListaAbierta());
    dto.setEstado(eleccion.getEstado() != null ? eleccion.getEstado().name() : null);
    dto.setCandidatos(candidatos);

    return dto;
  }

  public List<ResponseEleccionDTO> toResponseDTOs(List<Eleccion> elecciones) {
    return elecciones.stream().map(this::toResponseDTO).toList();
  }

}
