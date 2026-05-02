package PortalVotacionBack.dtos.candidato;

import java.util.List;

import org.springframework.stereotype.Component;

import PortalVotacionBack.entities.Candidato;

@Component
public class MapperCandidato {

  public ResponseCandidatoDTO toResponseDTO(Candidato candidato) {
    if (candidato == null) return null;

    ResponseCandidatoDTO dto = new ResponseCandidatoDTO();
    dto.setIdCandidato(candidato.getIdCandidato());
    dto.setNombre(candidato.getNombre());
    dto.setNumero(candidato.getNumero());
    dto.setFotoUrl(candidato.getFotoUrl());
    dto.setActivo(candidato.getActivo());

    if (candidato.getLista() != null) {
      dto.setIdLista(candidato.getLista().getIdLista());
    }

    if (candidato.getPartido() != null) {
      dto.setIdPartido(candidato.getPartido().getIdPartido());
      dto.setNombrePartido(candidato.getPartido().getNombre());
      dto.setSiglaPartido(candidato.getPartido().getSigla());
      dto.setLogoPartido(candidato.getPartido().getLogoUrl());
    }

    return dto;
  }

  public List<ResponseCandidatoDTO> toResponseDTOs(List<Candidato> candidatos) {
    return candidatos.stream().map(this::toResponseDTO).toList();
  }

}
