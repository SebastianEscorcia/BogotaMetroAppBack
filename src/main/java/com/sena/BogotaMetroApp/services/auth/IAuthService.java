package com.sena.BogotaMetroApp.services.auth;

import com.sena.BogotaMetroApp.presentation.dto.auth.UserAfterAuthDTO;
import com.sena.BogotaMetroApp.presentation.dto.login.AuthResponse;
import com.sena.BogotaMetroApp.presentation.dto.login.LoginRequest;

public interface IAuthService {
    AuthResponse login(LoginRequest request);

    void logout();

    void solicitarRecuperacion(String correo);

    void cambiarClave(String token, String nuevaClave);
    UserAfterAuthDTO obterMisDatos();
}
