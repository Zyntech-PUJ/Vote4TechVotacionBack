package PortalVotacionBack.dtos.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO raíz devuelto por GET /sync/datos-electorales.
 * Contiene todos los datos que ServidorLocalUrna necesita
 * para sincronizar su réplica local.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatosElectoralesDTO {

    private List<CiudadanoSyncDTO> ciudadanos;
    private List<PartidoSyncDTO> partidos;
    private List<EleccionSyncDTO> elecciones;
    private List<ListaSyncDTO> listas;
    private List<CandidatoSyncDTO> candidatos;
    private List<CentroVotacionSyncDTO> centrosVotacion;
    private List<MesaSyncDTO> mesas;
    private List<RegistradorSyncDTO> registradores;

}
