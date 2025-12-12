package com.sena.BogotaMetroApp.services.chatmensaje;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.externalservices.ChatRedisService;
import com.sena.BogotaMetroApp.persistence.models.Mensaje;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.sesionchat.SesionChat;
import com.sena.BogotaMetroApp.persistence.repository.MensajeRepository;
import com.sena.BogotaMetroApp.persistence.repository.ParticipanteSesionRepository;
import com.sena.BogotaMetroApp.persistence.repository.SesionChatRepository;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.presentation.dto.MensajeDTO;
import com.sena.BogotaMetroApp.services.exception.chat.ChatException;
import com.sena.BogotaMetroApp.utils.enums.EstadoSesionEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementación del servicio para el procesamiento y almacenamiento de mensajes en sesiones de chat.
 */
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
                .orElseThrow(() -> new ChatException(ErrorCodeEnum.CHAT_SESION_NOT_FOUND));

        Usuario remitente = usuarioRepository.findById(mensajeDTO.getRemitenteId())
                .orElseThrow(() -> new ChatException(ErrorCodeEnum.CHAT_REMITENTE_NOT_FOUND));

        validarReglasDeNegocio(sesion, remitente);


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
            throw new ChatException(ErrorCodeEnum.CHAT_CERRADO);
        }

        boolean esParticipante = participanteRepository
                .existsBySesionChatIdAndUsuarioIdAndActivoTrue(sesion.getId(), remitente.getId());

        if(!esParticipante) {
            throw new ChatException(ErrorCodeEnum.CHAT_ACCESO_DENEGADO);
        }
    }

}
