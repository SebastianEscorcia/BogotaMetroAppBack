package com.sena.BogotaMetroApp.services.factory;

import com.sena.BogotaMetroApp.persistence.models.DatosPersonales;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.RegistroPasajeroUnificadoDTO;
import org.springframework.stereotype.Component;

@Component
public class DatosPersonalesFactory {

    public DatosPersonales crearDesdeRegistro(RegistroPasajeroUnificadoDTO dto, Usuario usuario) {

        DatosPersonales dp = new DatosPersonales();
        dp.setUsuario(usuario);
        dp.setNombreCompleto(dto.getNombreCompleto());
        dp.setTelefono(dto.getTelefono());
        dp.setTipoDocumento(dto.getTipoDocumento());
        dp.setNumDocumento(dto.getNumDocumento());
        dp.setFechaNacimiento(dto.getFechaNacimiento());
        dp.setDireccion(dto.getDireccion());

        return dp;
    }
}