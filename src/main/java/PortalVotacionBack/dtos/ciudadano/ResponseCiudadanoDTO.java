package PortalVotacionBack.dtos.ciudadano;

import lombok.Data;

@Data
public class ResponseCiudadanoDTO {

  private Long idCiudadano;
  private String nombre;
  private String cedula;
  private String genero;
  private Boolean votoObligatorio;

}
