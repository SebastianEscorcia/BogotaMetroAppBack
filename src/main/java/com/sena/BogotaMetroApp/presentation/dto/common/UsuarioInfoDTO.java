package com.sena.BogotaMetroApp.presentation.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioInfoDTO {
    private Long idUsuario;
    private String correo;

    private String nombreCompleto;
    private String telefono;
    private String tipoDocumento;
    private String numDocumento;
}
