package PortalVotacionBack.services.etl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import PortalVotacionBack.couchdb.CouchDbService;
import PortalVotacionBack.entities.VotoSql;
import PortalVotacionBack.repositories.RepositoryVotoSql;

/**
 * Servicio ETL: extrae votos de ambas bases CouchDB (votos_urna y
 * votos_domicilio), transforma los documentos JSON en entidades relacionales y
 * los ingesta en la tabla VOTO_SQL de PostgreSQL.
 * La operación es idempotente: si un voto ya existe (mismo id) se ignora.
 */
@Service
public class EtlService {

  @Autowired
  private CouchDbService couchDbService;

  @Autowired
  private RepositoryVotoSql repositoryVotoSql;

  public EtlResultDTO ejecutar() {
    int nuevos = 0;
    int omitidos = 0;

    List<JsonNode> todosLosDocs = new ArrayList<>();
    todosLosDocs.addAll(couchDbService.getAllDocs(couchDbService.getDbUrna()));
    todosLosDocs.addAll(couchDbService.getAllDocs(couchDbService.getDbDomicilio()));

    for (JsonNode doc : todosLosDocs) {
      String id = doc.path("_id").asText();
      if (repositoryVotoSql.existsById(id)) {
        omitidos++;
        continue;
      }

      VotoSql voto = VotoSql.builder()
          .idVoto(id)
          .idEleccion(nullableLong(doc, "idEleccion"))
          .idMesa(nullableLong(doc, "idMesa"))
          .tipoMesa(doc.path("tipoMesa").asText(null))
          .idCentroVotacion(nullableLong(doc, "idCentroVotacion"))
          .tipoSeleccion(doc.path("tipoSeleccion").asText(null))
          .idSeleccion(nullableLong(doc, "idSeleccion"))
          .timestamp(doc.path("timestamp").asText(null))
          .build();

      repositoryVotoSql.save(voto);
      nuevos++;
    }

    return new EtlResultDTO(todosLosDocs.size(), nuevos, omitidos);
  }

  private Long nullableLong(JsonNode node, String field) {
    JsonNode n = node.path(field);
    return (n.isNull() || n.isMissingNode() || n.asText().isBlank()) ? null : n.asLong();
  }

}
