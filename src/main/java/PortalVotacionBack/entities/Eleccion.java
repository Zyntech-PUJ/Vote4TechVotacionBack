package PortalVotacionBack.entities;

import java.time.LocalDateTime;
import java.util.List;

import PortalVotacionBack.enums.EstadoEleccion;
import PortalVotacionBack.enums.TipoEleccion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "ELECCION")
public class Eleccion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_eleccion")
  private Long idEleccion;

  @Column(name = "nombre", nullable = false, length = 64)
  private String nombre;

  @Column(name = "fecha_inicio", nullable = false)
  private LocalDateTime fechaInicio;

  @Column(name = "fecha_finalizacion", nullable = false)
  private LocalDateTime fechaFinalizacion;

  @Column(name = "fecha_creacion", nullable = false, updatable = false)
  private LocalDateTime fechaCreacion;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo", nullable = false, length = 32)
  private TipoEleccion tipo;

  @Column(name = "lista_abierta", nullable = false)
  private Boolean listaAbierta;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", nullable = false, length = 32)
  private EstadoEleccion estado;

  @OneToMany(mappedBy = "eleccion")
  private List<Lista> listas;

}
