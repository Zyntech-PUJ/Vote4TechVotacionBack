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
@Table(name = "CENTRO_VOTACION")
public class CentroVotacion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_centro_votacion")
  private Long idCentroVotacion;

  @Column(name = "nombre", nullable = false, length = 128)
  private String nombre;

  @Column(name = "direccion", nullable = false, length = 256)
  private String direccion;

  @Column(name = "ciudad", nullable = false, length = 64)
  private String ciudad;

  @Column(name = "departamento", nullable = false, length = 64)
  private String departamento;

  @Column(name = "activo", nullable = false)
  private Boolean activo;

}
