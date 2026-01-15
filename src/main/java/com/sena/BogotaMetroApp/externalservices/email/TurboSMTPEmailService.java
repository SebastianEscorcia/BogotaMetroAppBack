package com.sena.BogotaMetroApp.externalservices.email;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TurboSMTPEmailService implements IEmailService {

    private final RestTemplate restTemplate;

    private static final String TURBO_SMTP_API_URL = "https://api.turbo-smtp.com/api/v2/mail/send";

    @Value("${turbosmtp.consumer.key}")
    private String consumerKey;

    @Value("${turbosmtp.consumer.secret}")
    private String consumerSecret;

    @Value("${turbosmtp.from.email}")
    private String fromEmail;

    @Value("${turbosmtp.from.name}")
    private String fromName;
    @Value("${app.frontend.url:http://localhost:5173/reset-password}")
    private String frontendUrl;


    /**
     * Envía un correo de recuperación de contraseña de forma asíncrona.
     * @param correoDestino El correo electrónico del destinatario.
     * @param token El token de recuperación de contraseña.
     */
    @Override
    @Async
    public void enviarEmailRecuperacion(String correoDestino, String token) {
        // /recuperar-clave
        String enlace = frontendUrl + "?token=" + token;

        log.info("Preparando envio de correo a: {}", correoDestino);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("consumerKey", consumerKey);
            headers.set("consumerSecret", consumerSecret);

            // Cuerpo según documentación de TurboSMTP
            Map<String, String> body = new HashMap<>();
            body.put("from", fromName + " <" + fromEmail + ">");
            body.put("to", correoDestino);
            body.put("subject", "Recuperacion de contrasena - Bogota Metro App");
            body.put("content", construirTextoPlano(enlace));
            body.put("html_content", construirHtml(enlace));

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            log.info("Enviando request a TurboSMTP API: {}", TURBO_SMTP_API_URL);
            log.info("Headers: consumerKey={}, consumerSecret=***", consumerKey);
            log.info("Body: from={}, to={}", body.get("from"), body.get("to"));

            ResponseEntity<String> response = restTemplate.exchange(
                    TURBO_SMTP_API_URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            log.info("Respuesta TurboSMTP: Status={}, Body={}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Correo enviado exitosamente a: {}", correoDestino);
            } else {
                log.error("Error en respuesta de TurboSMTP: {}", response.getBody());
            }

        } catch (Exception e) {
            log.error("Error enviando correo a {}: {}", correoDestino, e.getMessage(), e);
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
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><meta charset=\"UTF-8\">" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #1a237e 0%, #0d47a1 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                ".button { display: inline-block; background: #1a237e; color: white !important; padding: 15px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                ".footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }" +
                ".warning { background: #fff3cd; border: 1px solid #ffc107; padding: 10px; border-radius: 5px; margin-top: 20px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<div class=\"header\">" +
                "<h1>Bogota Metro App</h1>" +
                "<p>Recuperacion de Contrasena</p>" +
                "</div>" +
                "<div class=\"content\">" +
                "<h2>Hola,</h2>" +
                "<p>Hemos recibido una solicitud para restablecer la contrasena de tu cuenta.</p>" +
                "<p>Haz clic en el siguiente boton para crear una nueva contrasena:</p>" +
                "<div style=\"text-align: center;\">" +
                "<a href=\"" + enlace + "\" class=\"button\">Restablecer Contrasena</a>" +
                "</div>" +
                "<div class=\"warning\">" +
                "<strong>Este enlace expirara en 15 minutos.</strong>" +
                "</div>" +
                "<p>Si no solicitaste este cambio, puedes ignorar este correo.</p>" +
                "<p>Si el boton no funciona, copia y pega este enlace:</p>" +
                "<p style=\"word-break: break-all; color: #1a237e;\">" + enlace + "</p>" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>2025 Bogota Metro App - SENA</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}