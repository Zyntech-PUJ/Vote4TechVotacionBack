package PortalVotacionBack.couchdb;

import java.time.LocalDateTime;

import PortalVotacionBack.enums.TipoMesa;
import PortalVotacionBack.enums.TipoSeleccion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotoDocument {

  private String id;
  private Long idEleccion;
  private Long idMesa;
  private TipoMesa tipoMesa;
  private Long idCentroVotacion;
  private TipoSeleccion tipoSeleccion;
  private Long idSeleccion;
  private LocalDateTime timestamp;

}
