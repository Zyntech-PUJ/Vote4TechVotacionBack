package PortalVotacionBack.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "REGISTRADOR")
public class Registrador {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_registrador")
  private Long idRegistrador;

  @Column(name = "nombre", length = 128)
  private String nombre;

  @Column(name = "usuario", nullable = false, unique = true, length = 64)
  private String usuario;

  @Column(name = "password", nullable = false)
  private String password;

}
