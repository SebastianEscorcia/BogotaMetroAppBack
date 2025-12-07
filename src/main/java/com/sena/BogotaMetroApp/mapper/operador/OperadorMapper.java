package com.sena.BogotaMetroApp.mapper.operador;

import com.sena.BogotaMetroApp.mapper.UsuarioMapper;
import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.operador.Operador;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OperadorMapper {

    private final UsuarioMapper usuarioMapper;

    public OperadorResponseDTO toDTO(Operador op) {
        OperadorResponseDTO dto = new OperadorResponseDTO();

        dto.setId(op.getId());

        if (op.getUsuario() != null) {
            dto.setIdUsuario(op.getUsuario().getId());
            dto.setCorreo(op.getUsuario().getCorreo());
            var info = usuarioMapper.toUsuarioInfo(op.getUsuario());
            dto.setNombreCompleto(info.getNombreCompleto());
            dto.setTelefono(info.getTelefono());
            dto.setTipoDocumento(info.getTipoDocumento());
            dto.setNumDocumento(info.getNumDocumento());
        }

        return dto;

    }
}
