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
@Table(name = "CIUDADANO")
public class Ciudadano {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_ciudadano")
  private Long idCiudadano;

  @Column(name = "nombre", nullable = false, length = 64)
  private String nombre;

  @Column(name = "cedula", nullable = false, unique = true, length = 32)
  private String cedula;

  @Column(name = "genero", length = 1)
  private String genero;

  @Column(name = "voto_obligatorio", nullable = false)
  private Boolean votoObligatorio;

  /** Indica si el ciudadano está habilitado para voto domiciliario. */
  @Column(name = "habilitado_domicilio", nullable = false)
  private Boolean habilitadoDomicilio = false;

}
