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
import com.sena.BogotaMetroApp.services.exception.chat.ChatException;
import com.sena.BogotaMetroApp.utils.enums.EstadoSesionEnum;
import com.sena.BogotaMetroApp.utils.enums.TipoRemitenteEnum;
import com.sena.BogotaMetroApp.persistence.models.Role;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatMensajeServiceImplTest {

    @Mock
    private SesionChatRepository sesionChatRepository;

    @Mock
    private MensajeRepository mensajeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ParticipanteSesionRepository participanteRepository;

    @Mock
    private ChatRedisService chatRedisService;

    @InjectMocks
    private ChatMensajeServiceImpl chatService;

    private SesionChat sesion;
    private Usuario usuario;
    private MensajeDTO mensajeDTO;

    @BeforeEach
    void setUp() {
        sesion = new SesionChat();
        sesion.setId(1L);
        sesion.setEstado(EstadoSesionEnum.ACTIVO);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setActivo(true);

        mensajeDTO = new MensajeDTO();
        mensajeDTO.setRemitenteId(1L);
        mensajeDTO.setContenido("Hola");
        mensajeDTO.setTipoRemitente(TipoRemitenteEnum.PASAJERO);
    }

    @Test
    void testProcesarYGuardarMensaje() {

        when(sesionChatRepository.findById(1L)).thenReturn(Optional.of(sesion));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(participanteRepository.existsBySesionChatIdAndUsuarioIdAndActivoTrue(1L, 1L))
                .thenReturn(true);

        MensajeDTO result = chatService.procesarYGuardarMensaje(1L, mensajeDTO);

        assertNotNull(result.getFechaEnvio());

        verify(mensajeRepository).save(any(Mensaje.class));
        verify(sesionChatRepository).save(sesion);
        verify(chatRedisService).actualizarActividad(1L);
    }

    @Test
    void testSesionNoExiste() {
        when(sesionChatRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ChatException.class,
                () -> chatService.procesarYGuardarMensaje(1L, mensajeDTO));
    }

    @Test
    void testRemitenteNoExiste() {
        when(sesionChatRepository.findById(1L)).thenReturn(Optional.of(sesion));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ChatException.class,
                () -> chatService.procesarYGuardarMensaje(1L, mensajeDTO));
    }

    @Test
    void testChatCerrado() {
        sesion.setEstado(EstadoSesionEnum.ACTIVO);

        when(sesionChatRepository.findById(1L)).thenReturn(Optional.of(sesion));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThrows(ChatException.class,
                () -> chatService.procesarYGuardarMensaje(1L, mensajeDTO));
    }

    @Test
    void testAccesoDenegado() {

        when(sesionChatRepository.findById(1L)).thenReturn(Optional.of(sesion));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(participanteRepository.existsBySesionChatIdAndUsuarioIdAndActivoTrue(1L, 1L))
                .thenReturn(false);

        assertThrows(ChatException.class,
                () -> chatService.procesarYGuardarMensaje(1L, mensajeDTO));
    }

   @Test
   void testSoporteInactivo() {

    usuario.setActivo(false);

    Role rol = mock(Role.class);
    when(rol.getNombre()).thenReturn(TipoRemitenteEnum.SOPORTE.name());
    usuario.setRol(rol);

    when(sesionChatRepository.findById(1L)).thenReturn(Optional.of(sesion));
    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

    assertThrows(ChatException.class,
            () -> chatService.procesarYGuardarMensaje(1L, mensajeDTO));
}
}