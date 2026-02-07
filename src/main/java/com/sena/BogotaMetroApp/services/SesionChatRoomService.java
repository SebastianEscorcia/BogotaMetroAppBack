package com.sena.BogotaMetroApp.services;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.events.SesionCerradaEvent;
import com.sena.BogotaMetroApp.events.SesionCreadaEvent;
import com.sena.BogotaMetroApp.events.SesionTomadaEvent;
import com.sena.BogotaMetroApp.events.SoporteAsignadoEvent;
import com.sena.BogotaMetroApp.externalservices.ChatRedisService;
import com.sena.BogotaMetroApp.externalservices.cache.sesionchat.ISesionChatCacheService;
import com.sena.BogotaMetroApp.mapper.sesionchat.SesionChatMapper;
import com.sena.BogotaMetroApp.persistence.models.Mensaje;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.sesionchat.ParticipanteSesion;
import com.sena.BogotaMetroApp.persistence.models.sesionchat.SesionChat;
import com.sena.BogotaMetroApp.persistence.repository.MensajeRepository;
import com.sena.BogotaMetroApp.persistence.repository.ParticipanteSesionRepository;
import com.sena.BogotaMetroApp.persistence.repository.SesionChatRepository;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.presentation.dto.sesionchat.SesionChatResponseDTO;
import com.sena.BogotaMetroApp.services.exception.chat.ChatException;
import com.sena.BogotaMetroApp.utils.enums.EstadoSesionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SesionChatRoomService {
    private final SesionChatRepository sesionChatRepository;
    private final UsuarioRepository usuarioRepository;
    private final ParticipanteSesionRepository participanteRepository;
    private final MensajeRepository mensajeRepository;
    private final ChatRedisService chatRedisService;

    private final SesionChatMapper sesionChatMapper;

    private final ISesionChatCacheService sesionCacheService;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional()
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
        nuevaSesion.getParticipantes().add(participante);
        SesionChatResponseDTO dto = sesionChatMapper.toDTO(nuevaSesion);

        eventPublisher.publishEvent(new SesionCreadaEvent( dto) );
        log.info(" Nueva sesión creada, caché de pendientes invalidado");

        return nuevaSesion.getId();
    }

    @Transactional
    public void asignarSoporteASesion(Long idSesion, Long idUsuarioSoporte) {
        SesionChat sesion = sesionChatRepository.findByIdConBloqueo(idSesion)
                .orElseThrow(() -> new ChatException(ErrorCodeEnum.CHAT_SESION_NOT_FOUND));

        if (sesion.getEstado() != EstadoSesionEnum.PENDIENTE) {
            throw new ChatException(ErrorCodeEnum.CHAT_TOMADO_POR_SOPORTE);
        }

        boolean yaTieneSoporte = sesion.getParticipantes().stream()
                .anyMatch(p -> p.isActivo() &&
                        p.getUsuario().getRol().getNombre().equalsIgnoreCase("SOPORTE"));

        if (yaTieneSoporte) {
            throw new ChatException(ErrorCodeEnum.CHAT_TOMADO_POR_SOPORTE);
        }

        Usuario soporte = usuarioRepository.findById(idUsuarioSoporte)
                .orElseThrow(() -> new ChatException(ErrorCodeEnum.CHAT_SOPORTE_NOT_FOUND));

        if (!soporte.getRol().getNombre().equalsIgnoreCase("SOPORTE")) {
            throw new ChatException(ErrorCodeEnum.CHAT_SOPORTE_INVALIDO);
        }

        ParticipanteSesion participanteSoporte = new ParticipanteSesion();
        participanteSoporte.setSesionChat(sesion);
        participanteSoporte.setUsuario(soporte);
        participanteSoporte.setFechaUnion(LocalDateTime.now());
        participanteSoporte.setActivo(true);

        participanteRepository.save(participanteSoporte);

        sesion.setEstado(EstadoSesionEnum.ACTIVO);
        sesionChatRepository.save(sesion);

        String nombreSoporte = soporte.getDatosPersonales().getNombreCompleto() != null
                ? soporte.getDatosPersonales().getNombreCompleto()
                : soporte.getCorreo();
        eventPublisher.publishEvent(new SoporteAsignadoEvent(idSesion, nombreSoporte, idUsuarioSoporte));
        eventPublisher.publishEvent(new SesionTomadaEvent(idSesion, idUsuarioSoporte));
    }

    @Transactional
    public List<SesionChatResponseDTO> obtenerSesionesPendientesDTO() {
        Optional<List<SesionChatResponseDTO>> cached = sesionCacheService.obtenerSesionesPendientesCacheadas();
        if (cached.isPresent()) {
            log.info(" Sesiones pendientes obtenidas de caché Redis ({} sesiones)", cached.get().size());
            return cached.get();
        }
        List<SesionChat> sesionesPendientes = sesionChatRepository.findByEstadoWithParticipantes(EstadoSesionEnum.PENDIENTE);
        List<SesionChatResponseDTO> sesionesDTO = sesionesPendientes.stream()
                .map(sesionChatMapper::toDTO)
                .toList();
        sesionCacheService.cachearSesionesPendientes(sesionesDTO);

        return sesionesDTO;
    }

    @Transactional(readOnly = true)
    public List<SesionChatResponseDTO> obtenerSesionesActivasPorSoporte(Long idSoporte) {
        Optional<List<SesionChatResponseDTO>> cached = sesionCacheService.obtenerSesionesActivasCacheadas(idSoporte);
        if (cached.isPresent()) {
            log.info(" Sesiones activas del soporte {} obtenidas de caché Redis ({} sesiones)",
                    idSoporte, cached.get().size());
            return cached.get();
        }

        log.info(" Consultando sesiones activas del soporte {} en BD...", idSoporte);
        List<SesionChat> sesiones = participanteRepository
                .findSesionesActivasPorUsuarioOptimizado(idSoporte, List.of(EstadoSesionEnum.ACTIVO));

        List<SesionChatResponseDTO> resultado = sesiones.stream()
                .map(sesionChatMapper::toDTO)
                .toList();

        sesionCacheService.cachearSesionesActivas(idSoporte, resultado);

        return resultado;
    }

    @Transactional
    public SesionChatResponseDTO obtenerSesionPorId(Long idSesion) {
        SesionChat sesion = sesionChatRepository.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        return sesionChatMapper.toDTO(sesion);
    }

    @Transactional
    public List<Mensaje> obtenerMensajesPorSesion(Long idSesion) {
        return mensajeRepository.findBySesionChatIdOrderByFechaEnvioAsc(idSesion);
    }

    @Transactional(readOnly = true)
    public List<SesionChatResponseDTO> obtenerSesionesActivasPorSoporteDTO(Long idSoporte) {
        List<SesionChat> sesiones = participanteRepository
                .findSesionesActivasPorUsuarioOptimizado(idSoporte, List.of(EstadoSesionEnum.ACTIVO));
        return sesiones.stream()
                .map(sesionChatMapper::toDTO)
                .toList();
    }

    @Transactional
    public void cerrarSesion(Long idSesion) {
        SesionChat sesion = sesionChatRepository.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        Long idSoporte = obtenerIdSoporteDeSesion(sesion);

        sesion.setEstado(EstadoSesionEnum.CERRADO);
        sesion.setFechaCierre(LocalDateTime.now());
        sesionChatRepository.save(sesion);

        eventPublisher.publishEvent( new SesionCerradaEvent(idSesion, idSoporte));

    }

    private Long obtenerIdSoporteDeSesion(SesionChat sesion) {
        return sesion.getParticipantes().stream()
                .filter(p -> p.getUsuario().getRol().getNombre().equalsIgnoreCase("SOPORTE"))
                .filter(ParticipanteSesion::isActivo)
                .map(p -> p.getUsuario().getId())
                .findFirst()
                .orElse(null);
    }

}
