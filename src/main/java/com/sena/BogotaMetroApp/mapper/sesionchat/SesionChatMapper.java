package com.sena.BogotaMetroApp.mapper.sesionchat;

import com.sena.BogotaMetroApp.persistence.models.sesionchat.SesionChat;
import com.sena.BogotaMetroApp.presentation.dto.sesionchat.ParticipanteDTO;
import com.sena.BogotaMetroApp.presentation.dto.sesionchat.SesionChatResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class SesionChatMapper {
    public SesionChatResponseDTO toDTO(SesionChat sesion) {
        SesionChatResponseDTO dto = new SesionChatResponseDTO();
        dto.setId(sesion.getId());
        dto.setEstado(sesion.getEstado().name());
        dto.setFechaUltimaActividad(sesion.getFechaUltimaActividad());
        dto.setFechaAsignacion(sesion.getFechaAsignacion());

        dto.setParticipantes(sesion.getParticipantes().stream().map(p -> {
            ParticipanteDTO pDto = new ParticipanteDTO();
            pDto.setId(p.getId());
            pDto.setIdUsuario(p.getUsuario().getId());
            pDto.setNombreUsuario(p.getUsuario().getDatosPersonales() != null
                    ? p.getUsuario().getDatosPersonales().getNombreCompleto()
                    : p.getUsuario().getCorreo());
            pDto.setCorreo(p.getUsuario().getCorreo());
            pDto.setFechaUnion(p.getFechaUnion());
            pDto.setActivo(p.isActivo());
            return pDto;
        }).toList());

        return dto;
    }
}
