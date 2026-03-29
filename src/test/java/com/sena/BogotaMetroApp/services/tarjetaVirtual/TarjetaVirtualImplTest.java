package com.sena.BogotaMetroApp.services.tarjetaVirtual;

import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.repository.TarjetaVirtualRepository;
import com.sena.BogotaMetroApp.services.exception.pago.PagoException;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import com.sena.BogotaMetroApp.services.tarjetavirtual.TarjetaVirtualImpl;
import com.sena.BogotaMetroApp.utils.enums.EstadoTarjetaEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TarjetaVirtualImplTest {

    @Mock
    private TarjetaVirtualRepository repository;

    @InjectMocks
    private TarjetaVirtualImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDescontarSaldo_ok() {
        TarjetaVirtual tarjeta = new TarjetaVirtual();
        tarjeta.setSaldo(BigDecimal.valueOf(1000));
        tarjeta.setEstado(EstadoTarjetaEnum.ACTIVA);

        when(repository.findByPasajeroUsuarioId(1L))
                .thenReturn(Optional.of(tarjeta));

        service.descontarSaldo(1L, BigDecimal.valueOf(200));

        assertEquals(BigDecimal.valueOf(800), tarjeta.getSaldo());
        verify(repository).save(tarjeta);
    }

    @Test
    void testDescontarSaldo_noExisteTarjeta() {
        when(repository.findByPasajeroUsuarioId(1L))
                .thenReturn(Optional.empty());

        assertThrows(UsuarioException.class, () -> {
            service.descontarSaldo(1L, BigDecimal.valueOf(100));
        });
    }

    @Test
    void testDescontarSaldo_tarjetaInactiva() {
        TarjetaVirtual tarjeta = new TarjetaVirtual();
        tarjeta.setSaldo(BigDecimal.valueOf(1000));
        tarjeta.setEstado(EstadoTarjetaEnum.BLOQUEADA);

        when(repository.findByPasajeroUsuarioId(1L))
                .thenReturn(Optional.of(tarjeta));

        assertThrows(PagoException.class, () -> {
            service.descontarSaldo(1L, BigDecimal.valueOf(100));
        });
    }

    @Test
    void testDescontarSaldo_saldoInsuficiente() {
        TarjetaVirtual tarjeta = new TarjetaVirtual();
        tarjeta.setSaldo(BigDecimal.valueOf(50));
        tarjeta.setEstado(EstadoTarjetaEnum.ACTIVA);

        when(repository.findByPasajeroUsuarioId(1L))
                .thenReturn(Optional.of(tarjeta));

        assertThrows(PagoException.class, () -> {
            service.descontarSaldo(1L, BigDecimal.valueOf(100));
        });
    }

    @Test
    void testRecargarSaldo_ok() {
        TarjetaVirtual tarjeta = new TarjetaVirtual();
        tarjeta.setSaldo(BigDecimal.valueOf(500));

        when(repository.findByPasajeroUsuarioId(1L))
                .thenReturn(Optional.of(tarjeta));

        when(repository.save(any(TarjetaVirtual.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TarjetaVirtual result = service.recargarSaldo(1L, BigDecimal.valueOf(200));

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(700), result.getSaldo());
    }

    @Test
    void testRecargarSaldo_noExisteTarjeta() {
        when(repository.findByPasajeroUsuarioId(1L))
                .thenReturn(Optional.empty());

        assertThrows(UsuarioException.class, () -> {
            service.recargarSaldo(1L, BigDecimal.valueOf(100));
        });
    }
}