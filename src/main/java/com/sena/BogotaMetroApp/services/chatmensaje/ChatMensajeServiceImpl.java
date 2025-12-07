package com.sena.BogotaMetroApp.services.chatmensaje;

import com.sena.BogotaMetroApp.externalservices.ChatRedisService;
import com.sena.BogotaMetroApp.persistence.models.Mensaje;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.sesionchat.SesionChat;
import com.sena.BogotaMetroApp.persistence.repository.MensajeRepository;
import com.sena.BogotaMetroApp.persistence.repository.ParticipanteSesionRepository;
import com.sena.BogotaMetroApp.persistence.repository.SesionChatRepository;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.presentation.dto.MensajeDTO;
import com.sena.BogotaMetroApp.utils.enums.EstadoSesionEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMensajeServiceImpl implements IChatMensajeService {

    private final SesionChatRepository sesionChatRepository;
    private final MensajeRepository mensajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final ParticipanteSesionRepository participanteRepository;

    private final ChatRedisService chatRedisService;
    @Override
    @Transactional
    public MensajeDTO procesarYGuardarMensaje(Long idSesion, MensajeDTO mensajeDTO) {

        SesionChat sesion = sesionChatRepository.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("La sesión de chat no existe."));

        Usuario remitente = usuarioRepository.findById(mensajeDTO.getRemitenteId())
                .orElseThrow(() -> new RuntimeException("El usuario remitente no existe."));

        validarReglasDeNegocio(sesion, remitente);

        // 3. Mapeo y Persistencia
        Mensaje mensaje = new Mensaje();
        mensaje.setContenido(mensajeDTO.getContenido());
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setSesionChat(sesion);
        mensaje.setRemitente(remitente);
        mensaje.setTipoRemitente(mensajeDTO.getTipoRemitente());

        mensajeRepository.save(mensaje);

        sesion.setFechaUltimaActividad(LocalDateTime.now());
        sesionChatRepository.save(sesion);

        chatRedisService.actualizarActividad(idSesion);

        mensajeDTO.setFechaEnvio(mensaje.getFechaEnvio());

        return mensajeDTO;
    }


    private void validarReglasDeNegocio(SesionChat sesion, Usuario remitente) {
        // Regla 1: Chat Cerrado
        if (sesion.getEstado() == EstadoSesionEnum.CERRADO) {
            throw new RuntimeException("No se pueden enviar mensajes a una sesión cerrada.");
        }

        boolean esParticipante = participanteRepository
                .existsBySesionChatIdAndUsuarioIdAndActivoTrue(sesion.getId(), remitente.getId());

        if(!esParticipante) {
            throw new RuntimeException("Acceso denegado: Usted no es un participante activo de este chat.");
        }
    }

}
