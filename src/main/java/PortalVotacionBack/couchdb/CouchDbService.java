package PortalVotacionBack.couchdb;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import PortalVotacionBack.enums.TipoMesa;
import jakarta.annotation.PostConstruct;

@Service
public class CouchDbService {

  @Value("${couchdb.url}")
  private String couchDbUrl;

  @Value("${couchdb.username}")
  private String couchDbUsername;

  @Value("${couchdb.password}")
  private String couchDbPassword;

  @Value("${couchdb.database.urna:votos_urna}")
  private String dbUrna;

  @Value("${couchdb.database.domicilio:votos_domicilio}")
  private String dbDomicilio;

  private RestClient restClient;
  private ObjectMapper objectMapper;

  @PostConstruct
  public void init() {
    String credentials = couchDbUsername + ":" + couchDbPassword;
    String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

    this.restClient = RestClient.builder()
        .baseUrl(couchDbUrl)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoded)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    this.objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    ensureDatabaseExists(dbUrna);
    ensureDatabaseExists(dbDomicilio);
  }

  private void ensureDatabaseExists(String db) {
    try {
      restClient.put()
          .uri("/" + db)
          .retrieve()
          .toBodilessEntity();
    } catch (Exception e) {
      // La base de datos ya existe o no se puede crear — se continúa
    }
  }

  private String resolveDb(TipoMesa tipo) {
    return (tipo == TipoMesa.DOMICILIO) ? dbDomicilio : dbUrna;
  }

  public String saveVoto(VotoDocument voto) {
    String db = resolveDb(voto.getTipoMesa());
    try {
      Map<String, Object> doc = Map.of(
          "_id", voto.getId(),
          "idEleccion", voto.getIdEleccion(),
          "idMesa", voto.getIdMesa(),
          "tipoMesa", voto.getTipoMesa() != null ? voto.getTipoMesa().name() : null,
          "idCentroVotacion", voto.getIdCentroVotacion() != null ? voto.getIdCentroVotacion() : "",
          "tipoSeleccion", voto.getTipoSeleccion() != null ? voto.getTipoSeleccion().name() : null,
          "idSeleccion", voto.getIdSeleccion(),
          "timestamp", voto.getTimestamp() != null ? voto.getTimestamp().toString() : null
      );

      String body = objectMapper.writeValueAsString(doc);

      restClient.put()
          .uri("/" + db + "/" + voto.getId())
          .body(body)
          .retrieve()
          .toBodilessEntity();

      return voto.getId();
    } catch (Exception e) {
      throw new RuntimeException("Error al guardar voto en CouchDB (" + db + "): " + e.getMessage(), e);
    }
  }

  /**
   * Recupera todos los documentos de una base de datos CouchDB.
   * Usa _all_docs?include_docs=true para obtener el contenido completo.
   */
  public List<JsonNode> getAllDocs(String db) {
    try {
      String response = restClient.get()
          .uri("/" + db + "/_all_docs?include_docs=true")
          .retrieve()
          .body(String.class);

      JsonNode root = objectMapper.readTree(response);
      List<JsonNode> docs = new ArrayList<>();
      for (JsonNode row : root.path("rows")) {
        JsonNode doc = row.path("doc");
        // Ignorar documentos de diseño (_id empieza con _design/)
        if (!doc.path("_id").asText().startsWith("_design/")) {
          docs.add(doc);
        }
      }
      return docs;
    } catch (Exception e) {
      throw new RuntimeException("Error al leer votos de CouchDB (" + db + "): " + e.getMessage(), e);
    }
  }

  public String getDbUrna() { return dbUrna; }
  public String getDbDomicilio() { return dbDomicilio; }

}
