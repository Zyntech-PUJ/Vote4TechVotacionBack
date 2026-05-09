package PortalVotacionBack.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PortalVotacionBack.dtos.ciudadano.ResponseCiudadanoDTO;
import PortalVotacionBack.services.ciudadano.IServiceCiudadano;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/ciudadano")
@Tag(name = "Ciudadano", description = "Consulta de ciudadanos habilitados para votar")
public class ControllerCiudadano {

  @Autowired
  IServiceCiudadano servicesCiudadano;

  @GetMapping("/{cedula}")
  @Operation(summary = "Verificar ciudadano por cédula")
  public ResponseEntity<ResponseCiudadanoDTO> findByCedula(@PathVariable String cedula) {
    return ResponseEntity.ok(servicesCiudadano.findByCedula(cedula));
  }

  @GetMapping("/domicilio")
  @Operation(summary = "Obtener listado de ciudadanos habilitados para voto domiciliario")
  public ResponseEntity<List<ResponseCiudadanoDTO>> findHabilitadosDomicilio() {
    return ResponseEntity.ok(servicesCiudadano.findHabilitadosDomicilio());
  }

}
