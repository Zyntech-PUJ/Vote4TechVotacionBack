package PortalVotacionBack.services.ciudadano;

import PortalVotacionBack.dtos.ciudadano.ResponseCiudadanoDTO;

public interface IServiceCiudadano {

  ResponseCiudadanoDTO findByCedula(String cedula);

}
