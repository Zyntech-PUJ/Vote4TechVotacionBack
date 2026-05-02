package PortalVotacionBack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PortalVotacionBack.dtos.voto.CreateVotoDTO;
import PortalVotacionBack.dtos.voto.ResponseVotoDTO;
import PortalVotacionBack.services.voto.IServiceVoto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/voto")
@Tag(name = "Voto", description = "Registro y consulta de votos")
public class ControllerVoto {

  @Autowired
  IServiceVoto serviceVoto;

  @PostMapping("/votar")
  @Operation(summary = "Registrar un voto")
  public ResponseEntity<ResponseVotoDTO> votar(@RequestBody CreateVotoDTO dto) {
    return ResponseEntity.ok(serviceVoto.votar(dto));
  }

  @GetMapping("/ya-voto/{cedula}/{idEleccion}")
  @Operation(summary = "Verificar si un ciudadano ya votó en una elección")
  public ResponseEntity<Boolean> yaVoto(
      @PathVariable String cedula,
      @PathVariable Long idEleccion) {
    return ResponseEntity.ok(serviceVoto.yaVoto(cedula, idEleccion));
  }

}
