package PortalVotacionBack.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "YA_VOTO",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cedula", "id_eleccion"})
    }
)
public class YaVoto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_ya_voto")
  private Long idYaVoto;

  @Column(name = "cedula", nullable = false, length = 32)
  private String cedula;

  @Column(name = "id_eleccion", nullable = false)
  private Long idEleccion;

  @Column(name = "timestamp_voto", nullable = false)
  private LocalDateTime timestampVoto;

}
