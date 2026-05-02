package PortalVotacionBack.entities;

import PortalVotacionBack.enums.TipoMesa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "MESA")
public class Mesa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_mesa")
  private Long idMesa;

  @Column(name = "numero", nullable = false)
  private Integer numero;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo", nullable = false, length = 16)
  private TipoMesa tipo;

  @Column(name = "activo", nullable = false)
  private Boolean activo;

  @ManyToOne
  @JoinColumn(name = "id_centro_votacion", referencedColumnName = "id_centro_votacion", nullable = false)
  private CentroVotacion centroVotacion;

}
