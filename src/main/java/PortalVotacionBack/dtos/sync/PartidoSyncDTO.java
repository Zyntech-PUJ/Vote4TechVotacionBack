package PortalVotacionBack.dtos.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartidoSyncDTO {
    private Long idPartido;
    private String nombre;
    private String sigla;
    private String logoUrl;
}
