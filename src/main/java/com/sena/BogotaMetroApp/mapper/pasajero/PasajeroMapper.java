package com.sena.BogotaMetroApp.mapper.pasajero;

import com.sena.BogotaMetroApp.mapper.UsuarioMapper;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
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
            dto.setNumTarjetaVirtual(p.getTarjetaVirtual().getNumeroTarjeta());

        }else{
            dto.setSaldo(BigDecimal.ZERO);
        }


        var info = usuarioMapper.toUsuarioInfo(p.getUsuario());

        dto.setCorreo(info.getCorreo());
        dto.setNombreCompleto(info.getNombreCompleto());
        dto.setDireccion(info.getDireccion());
        dto.setFechaNacimiento(info.getFechaNacimiento());
        dto.setTelefono(info.getTelefono());
        dto.setTipoDocumento(info.getTipoDocumento());
        dto.setNumDocumento(info.getNumDocumento());

        return dto;
    }
}
