package PortalVotacionBack.services.ciudadano;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import PortalVotacionBack.dtos.ciudadano.MapperCiudadano;
import PortalVotacionBack.dtos.ciudadano.ResponseCiudadanoDTO;
import PortalVotacionBack.entities.Ciudadano;
import PortalVotacionBack.exceptions.ResourceNotFoundException;
import PortalVotacionBack.repositories.RepositoryCiudadano;

@Service
public class ServiceCiudadano implements IServiceCiudadano {

  @Autowired
  RepositoryCiudadano repositoryCiudadano;

  @Autowired
  MapperCiudadano mapperCiudadano;

  @Override
  public ResponseCiudadanoDTO findByCedula(String cedula) {
    Ciudadano ciudadano = repositoryCiudadano.findByCedula(cedula)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Ciudadano no encontrado con cédula: " + cedula));
    return mapperCiudadano.toResponseDTO(ciudadano);
  }

  @Override
  public List<ResponseCiudadanoDTO> findHabilitadosDomicilio() {
    return repositoryCiudadano.findByHabilitadoDomicilioTrue()
        .stream()
        .map(mapperCiudadano::toResponseDTO)
        .collect(Collectors.toList());
  }

}
