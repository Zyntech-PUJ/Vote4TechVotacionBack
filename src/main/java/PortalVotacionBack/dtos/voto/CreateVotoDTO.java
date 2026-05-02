package PortalVotacionBack.dtos.voto;

import lombok.Data;

@Data
public class CreateVotoDTO {

  private String cedula;
  private Long idEleccion;
  private Long idMesa;
  private String tipoSeleccion;
  private Long idSeleccion;

}
