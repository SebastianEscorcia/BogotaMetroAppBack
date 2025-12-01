package com.sena.BogotaMetroApp.mapper.pasajero;

import com.sena.BogotaMetroApp.persistence.models.DatosPersonales;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroUpdateDTO;
import org.springframework.stereotype.Component;

@Component
public class PasajeroMapper {
    public PasajeroResponseDTO toDTO(Pasajero p) {
        PasajeroResponseDTO dto = new PasajeroResponseDTO();
        dto.setId(p.getId());
        dto.setCorreo(p.getUsuario().getCorreo());

        dto.setIdUsuario(p.getUsuario().getId());
        if (p.getTarjetaVirtual() != null) {
            dto.setIdTarjetaVirtual(p.getTarjetaVirtual().getIdTarjeta());
        }

        dto.setCorreo(p.getUsuario().getCorreo());

        if (p.getUsuario().getDatosPersonales() != null) {
            dto.setNombreCompleto(p.getUsuario().getDatosPersonales().getNombreCompleto());
            dto.setTelefono(p.getUsuario().getDatosPersonales().getTelefono());
            dto.setTipoDocumento(p.getUsuario().getDatosPersonales().getTipoDocumento());
            dto.setNumDocumento(p.getUsuario().getDatosPersonales().getNumDocumento());
        }

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
