package PortalVotacionBack.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PARTIDO")
public class Partido {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_partido")
  private Long idPartido;

  @Column(name = "nombre", nullable = false)
  private String nombre;

  @Column(name = "sigla", nullable = false)
  private String sigla;

  @Column(name = "logo_url", nullable = false)
  private String logoUrl;

}
