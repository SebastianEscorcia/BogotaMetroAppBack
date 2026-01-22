package com.sena.BogotaMetroApp.mapper.operador;

import com.sena.BogotaMetroApp.mapper.UsuarioMapper;
import com.sena.BogotaMetroApp.persistence.models.DatosPersonales;
import com.sena.BogotaMetroApp.persistence.models.Role;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.operador.Operador;
import com.sena.BogotaMetroApp.presentation.dto.operador.RegistroOperadorDTO;
import com.sena.BogotaMetroApp.services.factory.DatosPersonalesFactory;
import com.sena.BogotaMetroApp.services.factory.UsuarioFactory;
import com.sena.BogotaMetroApp.utils.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OperadorMapper {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioFactory usuarioFactory;
    private final DatosPersonalesFactory datosPersonalesFactory;

    public Operador toEntity(RegistroOperadorDTO dto) {
        Usuario usuario = usuarioFactory.crearDesdeRegistro(dto, RoleEnum.OPERADOR.toString());

        DatosPersonales datosP = datosPersonalesFactory.crearDesdeRegistro(dto, usuario);
        usuario.setDatosPersonales(datosP);

        Operador operador = new Operador();
        operador.setUsuario(usuario);

        return operador;
    }

    public OperadorResponseDTO toDTO(Operador op) {
        OperadorResponseDTO dto = new OperadorResponseDTO();

        dto.setId(op.getId());

        if (op.getUsuario() != null) {
            dto.setIdUsuario(op.getUsuario().getId());
            dto.setCorreo(op.getUsuario().getCorreo());
            var info = usuarioMapper.toUsuarioInfo(op.getUsuario());
            if (info != null) {
                dto.setNombreCompleto(info.getNombreCompleto());
                dto.setTelefono(info.getTelefono());
                dto.setTipoDocumento(info.getTipoDocumento());
                dto.setNumDocumento(info.getNumDocumento());
            }
        }

        return dto;

    }
}
