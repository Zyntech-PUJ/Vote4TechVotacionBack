package PortalVotacionBack.entities;

import java.time.LocalDateTime;
import java.util.List;

import PortalVotacionBack.enums.TipoLista;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "LISTA")
public class Lista {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_lista")
  private Long idLista;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo", nullable = false)
  private TipoLista tipo;

  @Column(name = "fecha_creacion", nullable = false, updatable = false)
  private LocalDateTime fechaCreacion;

  @Column(name = "fecha_modificacion", nullable = false, updatable = false)
  private LocalDateTime fechaModificacion;

  @ManyToOne
  @JoinColumn(name = "id_eleccion", referencedColumnName = "id_eleccion", nullable = false)
  private Eleccion eleccion;

  @OneToMany(mappedBy = "lista")
  private List<Candidato> candidatos;

}
