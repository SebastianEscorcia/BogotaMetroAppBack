package com.sena.BogotaMetroApp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String remitente;


    /**
     * Envía un correo de recuperación de contraseña de forma asíncrona.
     * @param correoDestino
     * @param token
     * Falta usar servicio de email real cómo SMTP, SendGrid, etc.
     */
    @Async
    public void enviarEmailRecuperacion(String correoDestino, String token) {
        // Construimos el enlace
        // OJO: Asegúrate que el puerto 4200 coincida con tu frontend o usa el puerto donde abras el HTML
        // Si estás abriendo el HTML directo desde la carpeta, tendrás que copiar el token manualmente.
        String enlace = "http://localhost:4200/recuperar-clave?token=" + token;

        // --- MODO SIMULACIÓN (SOLO DESARROLLO) ---
        log.info("==================================================");
        log.info("📧 SIMULACIÓN DE ENVÍO DE CORREO");
        log.info("PARA: {}", correoDestino);
        log.info("ASUNTO: Recuperación de contraseña");
        log.info("MENSAJE: Haz clic en el siguiente enlace:");
        log.info("🔗 LINK: {}", enlace);
        log.info("==================================================");

        /*
           --- COMENTAMOS EL ENVÍO REAL PARA QUE NO FALLE ---
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remitente);
            message.setTo(correoDestino);
            message.setSubject("Recuperación de contraseña");
            message.setText("Link: " + enlace);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo");
        }
        */
    }

    /*@Async
    public void enviarEmailRecuperacion(String correoDestino, String token) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remitente);
            message.setTo(correoDestino);
            message.setSubject("Recuperación de contraseña - Bogotá Metro App");
            String enlace = "http://localhost:4200/recuperar-clave?token=" + token;
            String cuerpo = "Hola,\n\n" +
                    "Has solicitado restablecer tu contraseña.\n" +
                    "Por favor, haz clic en el siguiente enlace para continuar:\n\n" +
                    enlace + "\n\n" +
                    "Este enlace expirará en 15 minutos.\n" +
                    "Si no solicitaste esto, ignora este mensaje.";
            message.setText(cuerpo);
            mailSender.send(message);
            System.out.println("Correo enviado a " + correoDestino);

        } catch (Exception e) {
            System.err.println("Error enviando correo: " + e.getMessage());
            throw new RuntimeException("Error al enviar el correo de recuperación");
        }
    }
    */

}
