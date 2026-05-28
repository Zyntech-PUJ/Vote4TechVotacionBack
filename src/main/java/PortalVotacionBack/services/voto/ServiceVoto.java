package PortalVotacionBack.services.voto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import PortalVotacionBack.couchdb.CouchDbService;
import PortalVotacionBack.couchdb.VotoDocument;
import PortalVotacionBack.dtos.voto.CreateVotoDTO;
import PortalVotacionBack.dtos.voto.ResponseVotoDTO;
import PortalVotacionBack.entities.Eleccion;
import PortalVotacionBack.entities.Mesa;
import PortalVotacionBack.entities.YaVoto;
import PortalVotacionBack.enums.EstadoEleccion;
import PortalVotacionBack.enums.TipoSeleccion;
import PortalVotacionBack.exceptions.BadRequestException;
import PortalVotacionBack.exceptions.BusinessException;
import PortalVotacionBack.exceptions.ResourceNotFoundException;
import PortalVotacionBack.repositories.RepositoryCiudadano;
import PortalVotacionBack.repositories.RepositoryEleccion;
import PortalVotacionBack.repositories.RepositoryMesa;
import PortalVotacionBack.repositories.RepositoryYaVoto;

@Service
public class ServiceVoto implements IServiceVoto {

  @Autowired
  RepositoryCiudadano repositoryCiudadano;

  @Autowired
  RepositoryEleccion repositoryEleccion;

  @Autowired
  RepositoryMesa repositoryMesa;

  @Autowired
  RepositoryYaVoto repositoryYaVoto;

  @Autowired
  CouchDbService couchDbService;

  @Override
  @Transactional
  public ResponseVotoDTO votar(CreateVotoDTO dto) {
    // 1. Validar ciudadano existe
    if (!repositoryCiudadano.findByCedula(dto.getCedula()).isPresent())
      throw new ResourceNotFoundException(
          "Ciudadano no encontrado con cédula: " + dto.getCedula());

    // 2. Validar elección existe y está EN_CURSO
    Eleccion eleccion = repositoryEleccion.findById(dto.getIdEleccion())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Elección no encontrada con id: " + dto.getIdEleccion()));

    if (eleccion.getEstado() != EstadoEleccion.EN_CURSO)
      throw new BusinessException(
          "La elección no está en curso. Estado actual: " + eleccion.getEstado().name());

    // 3. Validar mesa existe
    Mesa mesa = repositoryMesa.findById(dto.getIdMesa())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Mesa no encontrada con id: " + dto.getIdMesa()));

    // 4. Validar tipo de selección
    TipoSeleccion tipoSeleccion;
    try {
      tipoSeleccion = TipoSeleccion.valueOf(dto.getTipoSeleccion().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new BadRequestException(
          "Tipo de selección inválido: " + dto.getTipoSeleccion() + ". Valores permitidos: CANDIDATO, LISTA");
    }

    // 5. Verificar que no haya votado ya
    if (repositoryYaVoto.existsByCedulaAndIdEleccion(dto.getCedula(), dto.getIdEleccion()))
      throw new BusinessException(
          "El ciudadano con cédula " + dto.getCedula() + " ya votó en esta elección.");

    LocalDateTime ahora = LocalDateTime.now();
    String votoId = UUID.randomUUID().toString();

    // 6. Guardar voto anónimo en CouchDB (sin cédula)
    VotoDocument votoDoc = VotoDocument.builder()
        .id(votoId)
        .idEleccion(eleccion.getIdEleccion())
        .idMesa(mesa.getIdMesa())
        .tipoMesa(mesa.getTipo())
        .idCentroVotacion(mesa.getCentroVotacion() != null
            ? mesa.getCentroVotacion().getIdCentroVotacion() : null)
        .tipoSeleccion(tipoSeleccion)
        .idSeleccion(dto.getIdSeleccion())
        .timestamp(ahora)
        .build();

    couchDbService.saveVoto(votoDoc);

    // 7. Registrar que el ciudadano ya votó en PostgreSQL (solo cédula + idEleccion)
    repositoryYaVoto.save(YaVoto.builder()
        .cedula(dto.getCedula())
        .idEleccion(dto.getIdEleccion())
        .timestampVoto(ahora)
        .build());

    ResponseVotoDTO response = new ResponseVotoDTO();
    response.setVotoId(votoId);
    response.setIdEleccion(eleccion.getIdEleccion());
    response.setIdMesa(mesa.getIdMesa());
    response.setTimestamp(ahora);
    response.setMensaje("Voto registrado exitosamente.");

    return response;
  }

  @Override
  public boolean yaVoto(String cedula, Long idEleccion) {
    return repositoryYaVoto.existsByCedulaAndIdEleccion(cedula, idEleccion);
  }

  @Override
  @Transactional
  public void registrarYaVoto(String cedula, Long idEleccion, java.time.LocalDateTime timestamp) {
    if (!repositoryYaVoto.existsByCedulaAndIdEleccion(cedula, idEleccion)) {
      repositoryYaVoto.save(YaVoto.builder()
          .cedula(cedula)
          .idEleccion(idEleccion)
          .timestampVoto(timestamp != null ? timestamp : java.time.LocalDateTime.now())
          .build());
    }
  }

}
