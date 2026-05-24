package PortalVotacionBack.dtos.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistradorSyncDTO {
    private Long idRegistrador;
    private String nombre;
    private String usuario;
    private String passwordHash;
}
