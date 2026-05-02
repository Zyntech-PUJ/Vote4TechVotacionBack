package PortalVotacionBack.dtos.eleccion;

import java.time.LocalDateTime;
import java.util.List;

import PortalVotacionBack.dtos.candidato.ResponseCandidatoDTO;
import lombok.Data;

@Data
public class ResponseEleccionDTO {

  private Long idEleccion;
  private String nombre;
  private LocalDateTime fechaInicio;
  private LocalDateTime fechaFinalizacion;
  private String tipo;
  private Boolean listaAbierta;
  private String estado;
  private List<ResponseCandidatoDTO> candidatos;

}
