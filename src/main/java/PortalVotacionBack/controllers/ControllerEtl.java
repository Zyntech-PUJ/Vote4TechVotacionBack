package PortalVotacionBack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PortalVotacionBack.services.etl.EtlResultDTO;
import PortalVotacionBack.services.etl.EtlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/etl")
@Tag(name = "ETL", description = "Proceso ETL: extrae votos de CouchDB e ingesta en PostgreSQL")
public class ControllerEtl {

  @Autowired
  private EtlService etlService;

  @PostMapping("/ejecutar")
  @Operation(
      summary = "Ejecutar ETL",
      description = "Lee votos de votos_urna y votos_domicilio en CouchDB, "
          + "transforma a SQL e inserta en la tabla VOTO_SQL de PostgreSQL. "
          + "La operación es idempotente: votos ya existentes se omiten."
  )
  public ResponseEntity<EtlResultDTO> ejecutar() {
    return ResponseEntity.ok(etlService.ejecutar());
  }

}
