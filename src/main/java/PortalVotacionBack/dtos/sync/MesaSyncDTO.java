package PortalVotacionBack.dtos.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MesaSyncDTO {
    private Long idMesa;
    private Integer numero;
    private String tipo;
    private Boolean activo;
    private Long idCentroVotacion;
}
