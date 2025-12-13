package com.sena.BogotaMetroApp.externalservices.email;

public interface IEmailService {

    void enviarEmailRecuperacion(String correoDestino, String token);

}
