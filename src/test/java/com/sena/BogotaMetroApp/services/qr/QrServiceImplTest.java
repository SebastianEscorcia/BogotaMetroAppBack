package com.sena.BogotaMetroApp.services.qr;

import com.sena.BogotaMetroApp.externalservices.cache.IQrCacheService;
import com.sena.BogotaMetroApp.mapper.qr.QrMapper;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.persistence.repository.qr.QrRepository;
import com.sena.BogotaMetroApp.presentation.dto.QrCacheDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.services.exception.qr.QrException;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import com.sena.BogotaMetroApp.utils.validators.QrValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrServiceImplTest {

    @Mock
    private QrRepository qrRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private QrMapper qrMapper;

    @Mock
    private QrValidator qrValidator;

    @Mock
    private IQrNoUsadoService qrNoUsadoService;

    @Mock
    private IQrCacheService<QrCacheDTO, Long> qrCacheService;

    @InjectMocks
    private QrServiceImpl service;

    private Usuario usuario;
    private Qr qr;
    private QrResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);

        qr = new Qr();
        qr.setId(1L);
        qr.setUsuario(usuario);
        qr.setConsumido(false);
        qr.setFechaExpiracion(LocalDateTime.now().plusMinutes(10));

        responseDTO = new QrResponseDTO();
    }

    @Test
    void testGenerarQrDesdeCache() {

        QrCacheDTO cacheDTO = new QrCacheDTO();
        cacheDTO.setFechaExpiracion(LocalDateTime.now().plusMinutes(10));
        cacheDTO.setConsumido(false);

        when(usuarioRepository.findByCorreo("test@mail.com")).thenReturn(Optional.of(usuario));
        when(qrCacheService.get(1L)).thenReturn(Optional.of(cacheDTO));
        when(qrMapper.toDTOFromCache(cacheDTO)).thenReturn(responseDTO);

        QrResponseDTO result = service.generarQrAcceso("test@mail.com");

        assertNotNull(result);
    }

    @Test
    void testGenerarQrDesdeBD() {

        when(usuarioRepository.findByCorreo("test@mail.com")).thenReturn(Optional.of(usuario));
        when(qrCacheService.get(1L)).thenReturn(Optional.empty());
        when(qrRepository.findQrActivoByUsuario(eq(1L), any())).thenReturn(Optional.of(qr));
        when(qrMapper.toDTO(qr)).thenReturn(responseDTO);

        QrResponseDTO result = service.generarQrAcceso("test@mail.com");

        assertNotNull(result);
        verify(qrCacheService).cache(any());
    }

    @Test
    void testGenerarQrUsuarioNoExiste() {

        when(usuarioRepository.findByCorreo("test@mail.com")).thenReturn(Optional.empty());

        assertThrows(UsuarioException.class,
                () -> service.generarQrAcceso("test@mail.com"));
    }

    @Test
    void testValidarQr() {

        when(qrRepository.findByContenidoQr("abc")).thenReturn(Optional.of(qr));

        Qr result = service.validarYObtenerPorContenido("abc");

        assertNotNull(result);
        verify(qrValidator).validarQrParaTorniquete(qr);
    }

    @Test
    void testValidarQrNoExiste() {

        when(qrRepository.findByContenidoQr("abc")).thenReturn(Optional.empty());

        assertThrows(QrException.class,
                () -> service.validarYObtenerPorContenido("abc"));
    }

    @Test
    void testConsumirQr() {

        when(qrRepository.findById(1L)).thenReturn(Optional.of(qr));
        when(qrRepository.save(qr)).thenReturn(qr);

        Qr result = service.consumirQr(qr);

        assertTrue(result.getConsumido());
        verify(qrCacheService).invalidate(1L);
    }

    @Test
    void testConsumirQrYaConsumido() {

        qr.setConsumido(true);

        when(qrRepository.findById(1L)).thenReturn(Optional.of(qr));

        Qr result = service.consumirQr(qr);

        assertTrue(result.getConsumido());
        verify(qrCacheService).invalidate(1L);
    }

    @Test
    void testConsumirQrNoExiste() {

        when(qrRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(QrException.class,
                () -> service.consumirQr(qr));
    }
}