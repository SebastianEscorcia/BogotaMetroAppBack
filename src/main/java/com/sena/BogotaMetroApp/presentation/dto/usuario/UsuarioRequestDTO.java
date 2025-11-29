package com.sena.BogotaMetroApp.presentation.dto.usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequestDTO {
    private String correo;
    private String clave;
    private Long idRol;
}

