package PortalVotacionBack.controllers;

import PortalVotacionBack.dtos.sync.*;
import PortalVotacionBack.entities.*;
import PortalVotacionBack.repositories.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoint de sincronización.
 * Devuelve todos los datos electorales en un único response
 * para que ServidorLocalUrna pueda sincronizar su réplica local.
 */
@RestController
@RequestMapping("/sync")
@Tag(name = "Sincronización", description = "Datos para sincronizar ServidorLocalUrna")
public class ControllerSync {

    private final RepositoryCiudadano repoCiudadano;
    private final RepositoryEleccion repoEleccion;
    private final RepositoryCandidato repoCandidato;
    private final RepositoryCentroVotacion repoCentroVotacion;
    private final RepositoryMesa repoMesa;
    private final RepositoryRegistrador repoRegistrador;

    public ControllerSync(RepositoryCiudadano repoCiudadano,
                          RepositoryEleccion repoEleccion,
                          RepositoryCandidato repoCandidato,
                          RepositoryCentroVotacion repoCentroVotacion,
                          RepositoryMesa repoMesa,
                          RepositoryRegistrador repoRegistrador) {
        this.repoCiudadano = repoCiudadano;
        this.repoEleccion = repoEleccion;
        this.repoCandidato = repoCandidato;
        this.repoCentroVotacion = repoCentroVotacion;
        this.repoMesa = repoMesa;
        this.repoRegistrador = repoRegistrador;
    }

    /**
     * Devuelve todos los datos electorales necesarios para la sincronización
     * con el servidor local de urna.
     */
    @GetMapping("/datos-electorales")
    @Operation(summary = "Obtener todos los datos electorales para sincronización")
    public ResponseEntity<DatosElectoralesDTO> getDatosElectorales() {

        List<CiudadanoSyncDTO> ciudadanos = repoCiudadano.findAll().stream()
                .map(c -> CiudadanoSyncDTO.builder()
                        .idCiudadano(c.getIdCiudadano())
                        .nombre(c.getNombre())
                        .cedula(c.getCedula())
                        .genero(c.getGenero())
                        .votoObligatorio(c.getVotoObligatorio())
                        .habilitadoDomicilio(c.getHabilitadoDomicilio())
                        .build())
                .toList();

        List<Eleccion> elecciones = repoEleccion.findAll();

        List<EleccionSyncDTO> eleccionDTOs = elecciones.stream()
                .map(e -> EleccionSyncDTO.builder()
                        .idEleccion(e.getIdEleccion())
                        .nombre(e.getNombre())
                        .fechaInicio(e.getFechaInicio())
                        .fechaFinalizacion(e.getFechaFinalizacion())
                        .fechaCreacion(e.getFechaCreacion())
                        .tipo(e.getTipo().name())
                        .listaAbierta(e.getListaAbierta())
                        .estado(e.getEstado().name())
                        .build())
                .toList();

        List<ListaSyncDTO> listas = elecciones.stream()
                .flatMap(e -> e.getListas().stream())
                .map(l -> ListaSyncDTO.builder()
                        .idLista(l.getIdLista())
                        .tipo(l.getTipo().name())
                        .fechaCreacion(l.getFechaCreacion())
                        .idEleccion(l.getEleccion().getIdEleccion())
                        .build())
                .toList();

        List<PartidoSyncDTO> partidos = repoCandidato.findAll().stream()
                .map(Candidato::getPartido)
                .distinct()
                .map(p -> PartidoSyncDTO.builder()
                        .idPartido(p.getIdPartido())
                        .nombre(p.getNombre())
                        .sigla(p.getSigla())
                        .logoUrl(p.getLogoUrl())
                        .build())
                .toList();

        List<CandidatoSyncDTO> candidatos = repoCandidato.findAll().stream()
                .map(c -> CandidatoSyncDTO.builder()
                        .idCandidato(c.getIdCandidato())
                        .nombre(c.getNombre())
                        .numero(c.getNumero())
                        .fotoUrl(c.getFotoUrl())
                        .activo(c.getActivo())
                        .idLista(c.getLista().getIdLista())
                        .idPartido(c.getPartido().getIdPartido())
                        .build())
                .toList();

        List<CentroVotacionSyncDTO> centros = repoCentroVotacion.findAll().stream()
                .map(cv -> CentroVotacionSyncDTO.builder()
                        .idCentroVotacion(cv.getIdCentroVotacion())
                        .nombre(cv.getNombre())
                        .direccion(cv.getDireccion())
                        .ciudad(cv.getCiudad())
                        .departamento(cv.getDepartamento())
                        .build())
                .toList();

        List<MesaSyncDTO> mesas = repoMesa.findAll().stream()
                .map(m -> MesaSyncDTO.builder()
                        .idMesa(m.getIdMesa())
                        .numero(m.getNumero())
                        .tipo(m.getTipo().name())
                        .activo(m.getActivo())
                        .idCentroVotacion(m.getCentroVotacion().getIdCentroVotacion())
                        .build())
                .toList();

        List<RegistradorSyncDTO> registradores = repoRegistrador.findAll().stream()
                .map(r -> RegistradorSyncDTO.builder()
                        .idRegistrador(r.getIdRegistrador())
                        .nombre(r.getNombre())
                        .usuario(r.getUsuario())
                        .passwordHash(r.getPassword())
                        .build())
                .toList();

        return ResponseEntity.ok(DatosElectoralesDTO.builder()
                .ciudadanos(ciudadanos)
                .partidos(partidos)
                .elecciones(eleccionDTOs)
                .listas(listas)
                .candidatos(candidatos)
                .centrosVotacion(centros)
                .mesas(mesas)
                .registradores(registradores)
                .build());
    }
}
