package PortalVotacionBack.dtos.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CiudadanoSyncDTO {
    private Long idCiudadano;
    private String nombre;
    private String cedula;
    private String genero;
    private Boolean votoObligatorio;
    private Boolean habilitadoDomicilio;
}
