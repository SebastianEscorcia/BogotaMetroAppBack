package com.sena.BogotaMetroApp.services;

import com.sena.BogotaMetroApp.externalservices.ChatRedisService;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.sesionchat.ParticipanteSesion;
import com.sena.BogotaMetroApp.persistence.models.sesionchat.SesionChat;
import com.sena.BogotaMetroApp.persistence.repository.ParticipanteSesionRepository;
import com.sena.BogotaMetroApp.persistence.repository.SesionChatRepository;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.utils.enums.EstadoSesionEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SesionChatRoomService {
    private final SesionChatRepository sesionChatRepository;
    private final UsuarioRepository usuarioRepository;
    private final ParticipanteSesionRepository participanteRepository;

    private final ChatRedisService chatRedisService;

    @Transactional
    public Long solicitarChatSoporte(Long idUsuarioSolicitante) {

        List<EstadoSesionEnum> estadosAbiertos = List.of(EstadoSesionEnum.PENDIENTE, EstadoSesionEnum.ACTIVO);

        Optional<SesionChat> sesionExistente = participanteRepository
                .findSesionActivaPorUsuario(idUsuarioSolicitante, estadosAbiertos);


        if (sesionExistente.isPresent()) {
            chatRedisService.actualizarActividad(sesionExistente.get().getId());
            return sesionExistente.get().getId();
        }

        Usuario usuario = usuarioRepository.findById(idUsuarioSolicitante)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        SesionChat nuevaSesion = new SesionChat();
        nuevaSesion.setEstado(EstadoSesionEnum.PENDIENTE);
        nuevaSesion.setFechaAsignacion(LocalDateTime.now());
        nuevaSesion.setFechaUltimaActividad(LocalDateTime.now());

        nuevaSesion = sesionChatRepository.save(nuevaSesion);


        ParticipanteSesion participante = new ParticipanteSesion();
        participante.setSesionChat(nuevaSesion);
        participante.setUsuario(usuario);
        participante.setFechaUnion(LocalDateTime.now());
        participante.setActivo(true);

        participanteRepository.save(participante);

        chatRedisService.actualizarActividad(nuevaSesion.getId());

        return nuevaSesion.getId();
    }

    // 2. El Soporte toma un chat de la lista de espera
    @Transactional
    public void asignarSoporteASesion(Long idSesion, Long idUsuarioSoporte) {
        SesionChat sesion = sesionChatRepository.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        Usuario soporte = usuarioRepository.findById(idUsuarioSoporte)
                .orElseThrow(() -> new RuntimeException("Usuario soporte no encontrado"));

        String nombreRol = soporte.getRol().getNombre();
        if (!nombreRol.equalsIgnoreCase("SOPORTE")) {
            throw new RuntimeException("Acceso denegado: Este usuario no tiene permisos");
        }


        ParticipanteSesion participanteSoporte = new ParticipanteSesion();
        participanteSoporte.setSesionChat(sesion);
        participanteSoporte.setUsuario(soporte);
        participanteSoporte.setFechaUnion(LocalDateTime.now());
        participanteSoporte.setActivo(true);

        participanteRepository.save(participanteSoporte);

        sesion.setEstado(EstadoSesionEnum.ACTIVO);
        sesionChatRepository.save(sesion);

        chatRedisService.actualizarActividad(idSesion);
    }

    @Transactional
    public void cerrarSesion(Long idSesion) {
        SesionChat sesion = sesionChatRepository.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        sesion.setEstado(EstadoSesionEnum.CERRADO);
        sesion.setFechaCierre(LocalDateTime.now());
        sesionChatRepository.save(sesion);

        // Borra el temporizador para que no salte el evento de expiración después
        chatRedisService.eliminarTemporizador(idSesion);
    }
}
