package PortalVotacionBack.dtos.ciudadano;

import org.springframework.stereotype.Component;

import PortalVotacionBack.entities.Ciudadano;

@Component
public class MapperCiudadano {

  public ResponseCiudadanoDTO toResponseDTO(Ciudadano ciudadano) {
    if (ciudadano == null) return null;

    ResponseCiudadanoDTO dto = new ResponseCiudadanoDTO();
    dto.setIdCiudadano(ciudadano.getIdCiudadano());
    dto.setNombre(ciudadano.getNombre());
    dto.setCedula(ciudadano.getCedula());
    dto.setGenero(ciudadano.getGenero());
    dto.setVotoObligatorio(ciudadano.getVotoObligatorio());
    dto.setHabilitadoDomicilio(ciudadano.getHabilitadoDomicilio());

    return dto;
  }

}
