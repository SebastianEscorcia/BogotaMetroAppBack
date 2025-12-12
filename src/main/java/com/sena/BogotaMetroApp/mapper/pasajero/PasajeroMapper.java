package com.sena.BogotaMetroApp.mapper.pasajero;

import com.sena.BogotaMetroApp.mapper.UsuarioMapper;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PasajeroMapper {

    private final UsuarioMapper usuarioMapper;

    public PasajeroResponseDTO toDTO(Pasajero p) {
        PasajeroResponseDTO dto = new PasajeroResponseDTO();
        dto.setId(p.getId());
        dto.setIdUsuario(p.getUsuario().getId());

        if (p.getTarjetaVirtual() != null) {
            dto.setIdTarjetaVirtual(p.getTarjetaVirtual().getIdTarjeta());
            dto.setSaldo(p.getTarjetaVirtual().getSaldo());

        }else{
            dto.setSaldo(BigDecimal.ZERO);
        }


        var info = usuarioMapper.toUsuarioInfo(p.getUsuario());

        dto.setCorreo(info.getCorreo());
        dto.setNombreCompleto(info.getNombreCompleto());
        dto.setTelefono(info.getTelefono());
        dto.setTipoDocumento(info.getTipoDocumento());
        dto.setNumDocumento(info.getNumDocumento());

        return dto;
    }

    public void updateEntity(PasajeroUpdateDTO dto, Pasajero pasajero) {
        if (dto.getNombreCompleto() != null) {
            pasajero.getUsuario().getDatosPersonales().setNombreCompleto(dto.getNombreCompleto());
        }
        if (dto.getTelefono() != null) {
            pasajero.getUsuario().getDatosPersonales().setTelefono(dto.getTelefono());
        }
        if (dto.getTipoDocumento() != null) {
            pasajero.getUsuario().getDatosPersonales().setTipoDocumento(dto.getTipoDocumento());
        }
        if (dto.getNumDocumento() != null) {
            pasajero.getUsuario().getDatosPersonales().setNumDocumento(dto.getNumDocumento());
        }
        if (dto.getFechaNacimiento() != null) {
            pasajero.getUsuario().getDatosPersonales().setFechaNacimiento(dto.getFechaNacimiento());
        }
        if (dto.getDireccion() != null) {
            pasajero.getUsuario().getDatosPersonales().setDireccion(dto.getDireccion());
        }
    }

}
