package PortalVotacionBack.services.ciudadano;

import java.util.List;

import PortalVotacionBack.dtos.ciudadano.ResponseCiudadanoDTO;

public interface IServiceCiudadano {

  ResponseCiudadanoDTO findByCedula(String cedula);

  List<ResponseCiudadanoDTO> findHabilitadosDomicilio();

}
