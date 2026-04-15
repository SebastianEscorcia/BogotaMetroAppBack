package com.sena.BogotaMetroApp.services.qrnousado;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.persistence.models.qr.QrNoUsado;
import com.sena.BogotaMetroApp.persistence.repository.qr.QrNoUsadoRepository;
import com.sena.BogotaMetroApp.services.qr.QrNoUsadoServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrNoUsadoServiceImplTest {

    @Mock
    private QrNoUsadoRepository qrNoUsadoRepository;

    @InjectMocks
    private QrNoUsadoServiceImpl service;

    private Qr qr;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);

        qr = new Qr();
        qr.setUsuario(usuario);
        qr.setContenidoQr("codigo123");
        qr.setFechaGeneracion(LocalDateTime.now().minusMinutes(10));
        qr.setFechaExpiracion(LocalDateTime.now());
        qr.setTipo(null); // puedes poner TipoQr.ACCESO si quieres
    }

    @Test
    void testMoverQrNoUsadoAndExpirado() {

        service.moverQrNoUsadoAndExpirado(qr);

        // Capturamos lo que se guarda
        ArgumentCaptor<QrNoUsado> captor = ArgumentCaptor.forClass(QrNoUsado.class);
        verify(qrNoUsadoRepository).save(captor.capture());

        QrNoUsado guardado = captor.getValue();

        assertNotNull(guardado);
        assertEquals(qr.getUsuario(), guardado.getUsuario());
        assertEquals(qr.getContenidoQr(), guardado.getContenidoQr());
        assertEquals(qr.getFechaGeneracion(), guardado.getFechaGeneracion());
        assertEquals(qr.getFechaExpiracion(), guardado.getFechaExpiracion());
        assertNotNull(guardado.getFechaMovimiento()); // importante
    }
}
