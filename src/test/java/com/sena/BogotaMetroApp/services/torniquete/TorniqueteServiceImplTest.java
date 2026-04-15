package com.sena.BogotaMetroApp.services.torniquete;

import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.persistence.repository.pasajero.PasajeroRepository;
import com.sena.BogotaMetroApp.persistence.repository.transaccion.TransaccionRepository;
import com.sena.BogotaMetroApp.presentation.dto.estacion.EstacionResponseDTO;
import com.sena.BogotaMetroApp.services.estacion.IEstacionServices;
import com.sena.BogotaMetroApp.services.exception.interrupcion.InterrupcionException;
import com.sena.BogotaMetroApp.services.exception.pago.PagoException;
import com.sena.BogotaMetroApp.services.horariosistema.IHorarioSistemaService;
import com.sena.BogotaMetroApp.services.interrupcion.IInterrupcionServices;
import com.sena.BogotaMetroApp.services.qr.IQrService;
import com.sena.BogotaMetroApp.services.tarifasistema.ITarifaSistemaService;
import com.sena.BogotaMetroApp.services.tarjetavirtual.ItarjetaVirtualService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TorniqueteServiceImplTest {

    @Mock
    private ItarjetaVirtualService tarjetaService;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private IEstacionServices estacionServices;

    @Mock
    private ITarifaSistemaService tarifaService;

    @Mock
    private IHorarioSistemaService horarioService;

    @Mock
    private IQrService qrService;

    @Mock
    private PasajeroRepository pasajeroRepository;

    @Mock
    private IInterrupcionServices interrupcionService;

    @InjectMocks
    private TorniqueteServiceImpl torniqueteService;

    private Usuario usuario;
    private Qr qr;
    private Pasajero pasajero;
    private TarjetaVirtual tarjeta;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);

        qr = new Qr();
        qr.setUsuario(usuario);

        tarjeta = new TarjetaVirtual();
        tarjeta.setSaldo(new BigDecimal("5000"));

        pasajero = new Pasajero();
        pasajero.setTarjetaVirtual(tarjeta);
    }

    @Test
    void testProcesarIngresoExitoso() {

        when(qrService.validarYObtenerPorContenido("QR123")).thenReturn(qr);
        when(horarioService.validarHorarioActual()).thenReturn(true);
        when(interrupcionService.tieneInterrupcionActiva(1L)).thenReturn(false);
        when(pasajeroRepository.findById(1L)).thenReturn(Optional.of(pasajero));
        when(tarifaService.obtenerValorTarifaActual()).thenReturn(new BigDecimal("2950"));

        EstacionResponseDTO estacion = new EstacionResponseDTO();
        estacion.setNombre("Estacion Central");
        when(estacionServices.obtener(1L)).thenReturn(estacion);

        assertDoesNotThrow(() -> torniqueteService.procesarIngreso("QR123", 1L));

        verify(tarjetaService).descontarSaldo(1L, new BigDecimal("2950"));
        verify(transaccionRepository).save(any());
        verify(qrService).consumirQr(qr);
    }

    @Test
    void testHorarioInvalido() {

        when(qrService.validarYObtenerPorContenido("QR123")).thenReturn(qr);
        when(horarioService.validarHorarioActual()).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> torniqueteService.procesarIngreso("QR123", 1L));

        assertEquals("Ingreso no permitido: Fuera del horario de operación del Metro", exception.getMessage());
    }

    @Test
    void testInterrupcionActiva() {

        when(qrService.validarYObtenerPorContenido("QR123")).thenReturn(qr);
        when(horarioService.validarHorarioActual()).thenReturn(true);
        when(interrupcionService.tieneInterrupcionActiva(1L)).thenReturn(true);

        assertThrows(InterrupcionException.class,
                () -> torniqueteService.procesarIngreso("QR123", 1L));
    }

    @Test
    void testPasajeroNoEncontrado() {

        when(qrService.validarYObtenerPorContenido("QR123")).thenReturn(qr);
        when(horarioService.validarHorarioActual()).thenReturn(true);
        when(interrupcionService.tieneInterrupcionActiva(1L)).thenReturn(false);
        when(pasajeroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PagoException.class,
                () -> torniqueteService.procesarIngreso("QR123", 1L));
    }

    @Test
    void testSaldoInsuficiente() {

        tarjeta.setSaldo(new BigDecimal("1000"));

        when(qrService.validarYObtenerPorContenido("QR123")).thenReturn(qr);
        when(horarioService.validarHorarioActual()).thenReturn(true);
        when(interrupcionService.tieneInterrupcionActiva(1L)).thenReturn(false);
        when(pasajeroRepository.findById(1L)).thenReturn(Optional.of(pasajero));
        when(tarifaService.obtenerValorTarifaActual()).thenReturn(new BigDecimal("2950"));

        assertThrows(PagoException.class,
                () -> torniqueteService.procesarIngreso("QR123", 1L));
    }
}