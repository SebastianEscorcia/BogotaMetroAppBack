package com.sena.BogotaMetroApp.presentation.dto.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String correo;
    private String clave;
}

