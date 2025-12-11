package com.sena.BogotaMetroApp.mapper.horariosistema;

import com.sena.BogotaMetroApp.persistence.models.horariosistema.HorarioSistema;
import com.sena.BogotaMetroApp.presentation.dto.horariosistema.HorarioSistemaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.horariosistema.HorarioSistemaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class HorarioSistemaMapper {

    public HorarioSistema toEntity(HorarioSistemaRequestDTO dto) {
        HorarioSistema horario = new HorarioSistema();
        horario.setDia(dto.getDia());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());
        horario.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        return horario;
    }

    public HorarioSistemaResponseDTO toDTO(HorarioSistema horario) {
        HorarioSistemaResponseDTO dto = new HorarioSistemaResponseDTO();
        dto.setId(horario.getId());
        dto.setDia(horario.getDia());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFin(horario.getHoraFin());
        dto.setActivo(horario.getActivo());
        return dto;
    }

    public void updateEntity(HorarioSistema horario, HorarioSistemaRequestDTO dto) {
        if (dto.getDia() != null) horario.setDia(dto.getDia());
        if (dto.getHoraInicio() != null) horario.setHoraInicio(dto.getHoraInicio());
        if (dto.getHoraFin() != null) horario.setHoraFin(dto.getHoraFin());
        if (dto.getActivo() != null) horario.setActivo(dto.getActivo());
    }
}
