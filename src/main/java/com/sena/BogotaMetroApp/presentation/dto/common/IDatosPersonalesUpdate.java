package com.sena.BogotaMetroApp.presentation.dto.common;

import com.sena.BogotaMetroApp.utils.enums.TipoDocumentoEnum;

import java.time.LocalDate;

public interface IDatosPersonalesUpdate {
    String getNombreCompleto();

    String getTelefono();

    TipoDocumentoEnum getTipoDocumento();

    String getNumDocumento();

    String getDireccion();

    LocalDate getFechaNacimiento();

    String getCorreo();
}
