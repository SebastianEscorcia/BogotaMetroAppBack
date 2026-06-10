package com.sena.BogotaMetroApp.externalservices.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class FailoverEmailService implements IEmailService {


    private final TurboSMTPEmailService turboSMTPEmailService;
    private final MailtrapEmailService mailtrapEmailService;

    @Override
    @Async
    public void enviarEmailRecuperacion(String correoDestino, String token) {
        try {
            log.info("[Failover] Intentando enviar correo con el proveedor principal (TurboSMTP)...");
            turboSMTPEmailService.enviarEmailRecuperacion(correoDestino, token);
        } catch (Exception e) {
            log.warn("[Failover] El proveedor principal (TurboSMTP) ha fallado: {}. Activando servicio de respaldo (Mailtrap)...", e.getMessage());
            try {
                mailtrapEmailService.enviarEmailRecuperacion(correoDestino, token);
            } catch (Exception ex) {
                log.error("[Failover] Catástrofe: El proveedor de respaldo (Mailtrap) también falló.", ex);
            }
        }
    }
}
