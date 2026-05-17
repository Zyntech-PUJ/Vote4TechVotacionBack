package PortalVotacionBack.dtos.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListaSyncDTO {
    private Long idLista;
    private String tipo;
    private LocalDateTime fechaCreacion;
    private Long idEleccion;
}
