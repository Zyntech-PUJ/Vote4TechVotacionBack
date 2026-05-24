package PortalVotacionBack.dtos.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoSyncDTO {
    private Long idCandidato;
    private String nombre;
    private String numero;
    private String fotoUrl;
    private Boolean activo;
    private Long idLista;
    private Long idPartido;
}
