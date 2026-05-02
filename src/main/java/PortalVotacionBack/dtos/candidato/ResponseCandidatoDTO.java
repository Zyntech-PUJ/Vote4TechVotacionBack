package PortalVotacionBack.dtos.candidato;

import lombok.Data;

@Data
public class ResponseCandidatoDTO {

  private Long idCandidato;
  private String nombre;
  private String numero;
  private String fotoUrl;
  private Boolean activo;
  private Long idLista;
  private Long idPartido;
  private String nombrePartido;
  private String siglaPartido;
  private String logoPartido;

}
