package com.sena.BogotaMetroApp.presentation.dto.usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponseDTO {

    private Long id;
    private String correo;
    private String rol;
    private String nombreCompleto;
}