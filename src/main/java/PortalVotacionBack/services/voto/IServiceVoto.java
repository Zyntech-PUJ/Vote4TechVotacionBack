package PortalVotacionBack.services.voto;

import PortalVotacionBack.dtos.voto.CreateVotoDTO;
import PortalVotacionBack.dtos.voto.ResponseVotoDTO;

public interface IServiceVoto {

  ResponseVotoDTO votar(CreateVotoDTO dto);

  boolean yaVoto(String cedula, Long idEleccion);

  void registrarYaVoto(String cedula, Long idEleccion, java.time.LocalDateTime timestamp);

}
