package PortalVotacionBack.dtos.voto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RegistrarYaVotoDTO {
    private String cedula;
    private Long idEleccion;
    private LocalDateTime timestamp;
}
