package PortalVotacionBack.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PortalVotacionBack.dtos.candidato.ResponseCandidatoDTO;
import PortalVotacionBack.dtos.eleccion.ResponseEleccionDTO;
import PortalVotacionBack.services.eleccion.IServiceEleccion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/eleccion")
@Tag(name = "Eleccion", description = "Consulta de elecciones y candidatos")
public class ControllerEleccion {

  @Autowired
  IServiceEleccion serviceEleccion;

  @GetMapping("/activas")
  @Operation(summary = "Listar elecciones en curso")
  public ResponseEntity<List<ResponseEleccionDTO>> findActivas() {
    return ResponseEntity.ok(serviceEleccion.findActivas());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener detalle de una elección con sus candidatos")
  public ResponseEntity<ResponseEleccionDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(serviceEleccion.findById(id));
  }

  @GetMapping("/{id}/candidatos")
  @Operation(summary = "Listar candidatos activos de una elección")
  public ResponseEntity<List<ResponseCandidatoDTO>> findCandidatosByEleccion(@PathVariable Long id) {
    return ResponseEntity.ok(serviceEleccion.findCandidatosByEleccion(id));
  }

}
