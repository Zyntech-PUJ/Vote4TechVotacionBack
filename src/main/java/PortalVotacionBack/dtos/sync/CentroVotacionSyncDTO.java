package PortalVotacionBack.dtos.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CentroVotacionSyncDTO {
    private Long idCentroVotacion;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String departamento;
}
