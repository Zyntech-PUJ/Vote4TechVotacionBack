package PortalVotacionBack.dtos.voto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResponseVotoDTO {

  private String votoId;
  private Long idEleccion;
  private Long idMesa;
  private LocalDateTime timestamp;
  private String mensaje;

}
