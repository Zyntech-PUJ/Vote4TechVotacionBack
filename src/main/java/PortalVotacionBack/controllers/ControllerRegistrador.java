package PortalVotacionBack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PortalVotacionBack.entities.Registrador;
import PortalVotacionBack.repositories.RepositoryRegistrador;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@RestController
@RequestMapping("/registrador")
@Tag(name = "Registrador", description = "Autenticación del registrador en la máquina de votación")
public class ControllerRegistrador {

  @Autowired
  private RepositoryRegistrador repositoryRegistrador;

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Data
  static class LoginRegistradorDTO {
    private String usuario;
    private String password;
  }

  @PostMapping("/login")
  @Operation(summary = "Login del registrador para habilitar la máquina de votación")
  public ResponseEntity<String> login(@RequestBody LoginRegistradorDTO dto) {
    Registrador registrador = repositoryRegistrador.findByUsuario(dto.getUsuario())
        .orElse(null);

    if (registrador == null || !passwordEncoder.matches(dto.getPassword(), registrador.getPassword())) {
      return ResponseEntity.status(401).body("Credenciales inválidas.");
    }

    // Devuelve el nombre del registrador como confirmación (no necesita JWT aquí —
    // el frontend gestiona la sesión en memoria/sessionStorage)
    return ResponseEntity.ok(registrador.getNombre());
  }

}
