package PortalVotacionBack.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "CANDIDATO")
public class Candidato {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_candidato")
  private Long idCandidato;

  @Column(name = "nombre", nullable = false, length = 32)
  private String nombre;

  @Column(name = "numero", nullable = false, length = 16)
  private String numero;

  @Column(name = "foto_url", nullable = false)
  private String fotoUrl;

  @Column(name = "activo", nullable = false)
  private Boolean activo;

  @ManyToOne
  @JoinColumn(name = "id_lista", referencedColumnName = "id_lista", nullable = false)
  private Lista lista;

  @ManyToOne
  @JoinColumn(name = "id_partido", referencedColumnName = "id_partido", nullable = false)
  private Partido partido;

}
