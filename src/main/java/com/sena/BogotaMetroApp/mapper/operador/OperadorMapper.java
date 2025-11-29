package com.sena.BogotaMetroApp.mapper.operador;

import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.operador.Operador;
import org.springframework.stereotype.Component;

@Component
public class OperadorMapper {

    public OperadorResponseDTO toDTO(Operador op) {
        OperadorResponseDTO dto = new OperadorResponseDTO();

        dto.setId(op.getId());
        dto.setCorreo(op.getUsuario().getCorreo());
        return dto;

    }
}
