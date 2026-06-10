package com.sena.BogotaMetroApp.externalservices.email;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MailtrapEmailService implements IEmailService{

    private MailtrapClient mailtrapClient;

    @Value("${mailtrap.token}")
    private String token;
    @Value("${mailtrap.from.email}")
    private String fromEmail;
    @Value("${mailtrap.from.name}")
    private String fromName;

    @Value("${app.frontend.url:http://localhost:5173/reset-password}")
    private String frontendUrl;


    @PostConstruct
    public void init() {
        MailtrapConfig config = new MailtrapConfig.Builder()
                .token(token)
                .build();
        this.mailtrapClient = MailtrapClientFactory.createMailtrapClient(config);
    }
    @Override
    @Async
    public void enviarEmailRecuperacion(String correoDestino, String token) {
        String enlace = frontendUrl + "?token=" + token;
        log.info("[Mailtrap] Preparando envío de correo de respaldo a: {}", correoDestino);

        try {
            MailtrapMail mail = MailtrapMail.builder()
                    .from(new Address(fromEmail, fromName))
                    .to(List.of(new Address(correoDestino)))
                    .subject("Recuperacion de contrasena - Bogota Metro App (Respaldo)")
                    .text(construirTextoPlano(enlace))
                    .html(construirHtml(enlace))
                    .build();

            var response = mailtrapClient.send(mail);
            log.info("[Mailtrap] Correo enviado exitosamente. Respuesta: {}", response);

        } catch (Exception e) {
            log.error("[Mailtrap] Error crítico en el servicio de respaldo al enviar a {}: {}",
                    correoDestino, e.getMessage(), e);
            // Lanzamos la excepción para que el orquestador sepa que ambos fallaron
            throw new RuntimeException("Ambos proveedores de correo (TurboSMTP y Mailtrap) han fallado", e);
        }
    }
    private String construirTextoPlano(String enlace) {
        return "Hola,\n\n" +
                "Hemos recibido una solicitud para restablecer la contrasena de tu cuenta.\n\n" +
                "Haz clic en el siguiente enlace para crear una nueva contrasena:\n" +
                enlace + "\n\n" +
                "Este enlace expirara en 15 minutos.\n\n" +
                "Si no solicitaste este cambio, puedes ignorar este correo.\n\n" +
                "Bogota Metro App - SENA 2025";
    }

    private String construirHtml(String enlace) {
        return "<!DOCTYPE html><html><head><meta charset=\"UTF-8\">" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #1a237e 0%, #0d47a1 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                ".button { display: inline-block; background: #1a237e; color: white !important; padding: 15px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                ".footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }" +
                ".warning { background: #fff3cd; border: 1px solid #ffc107; padding: 10px; border-radius: 5px; margin-top: 20px; }" +
                "</style></head><body>" +
                "<div class=\"container\"><div class=\"header\"><h1>Bogota Metro App</h1><p>Recuperacion de Contrasena</p></div>" +
                "<div class=\"content\"><h2>Hola,</h2>" +
                "<p>Hemos recibido una solicitud para restablecer la contrasena de tu cuenta.</p>" +
                "<p>Haz clic en el siguiente boton para crear una nueva contrasena:</p>" +
                "<div style=\"text-align: center;\"><a href=\"" + enlace + "\" class=\"button\">Restablecer Contrasena</a></div>" +
                "<div class=\"warning\"><strong>Este enlace expirara en 15 minutos.</strong></div>" +
                "<p>Si no solicitaste este cambio, puedes ignorar este correo.</p>" +
                "<p>Si el boton no funciona, copia y pega este enlace:</p>" +
                "<p style=\"word-break: break-all; color: #1a237e;\">" + enlace + "</p></div>" +
                "<div class=\"footer\"><p>2025 Bogota Metro App - SENA</p></div></div>" +
                "</body></html>";
    }
}
