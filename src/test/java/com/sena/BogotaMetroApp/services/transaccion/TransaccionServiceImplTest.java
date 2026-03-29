package com.sena.BogotaMetroApp.services.transaccion;

import com.sena.BogotaMetroApp.mapper.pago.TransaccionMapper;
import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.persistence.models.transaccion.PasarSaldo;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Recarga;
import com.sena.BogotaMetroApp.persistence.repository.TarjetaVirtualRepository;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.persistence.repository.transaccion.RecargaRepository;
import com.sena.BogotaMetroApp.persistence.repository.transaccion.TransaccionRepository;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionResponseDTO;
import com.sena.BogotaMetroApp.services.tarjetavirtual.ItarjetaVirtualService;
import com.sena.BogotaMetroApp.utils.enums.EstadoTarjetaEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransaccionServiceImplTest {

    @Mock 
    private UsuarioRepository usuarioRepository;

    @Mock
    private TransaccionMapper mapper;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private RecargaRepository recargaRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private TarjetaVirtualRepository tarjetaRepository;

    @Mock
    private ItarjetaVirtualService tarjetaService;

    @InjectMocks
    private TransaccionServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

   @Test
   void testRegistrarRecarga() {
    TransaccionRequestDTO dto = new TransaccionRequestDTO();

    Recarga recarga = new Recarga();
    TransaccionResponseDTO response = new TransaccionResponseDTO();

    var usuario = new com.sena.BogotaMetroApp.persistence.models.Usuario();
    usuario.setCorreo("test@mail.com");

    recarga.setUsuario(usuario);

    when(mapper.toRecargaEntity(dto)).thenReturn(recarga);
    when(usuarioRepository.findById(any()))
            .thenReturn(Optional.of(usuario)); 
    when(transaccionRepository.save(recarga)).thenReturn(recarga);
    when(mapper.toDTO(recarga)).thenReturn(response);

    TransaccionResponseDTO result = service.registrarRecarga(dto);

    assertNotNull(result);
    verify(eventPublisher).publishEvent(any(Object.class));
}

    @Test
    void testObtenerTransaccionPorId() {
        Recarga transaccion = new Recarga();
        TransaccionResponseDTO response = new TransaccionResponseDTO();

        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));
        when(mapper.toDTO(transaccion)).thenReturn(response);

        TransaccionResponseDTO result = service.obtenerTransaccionPorId(1L);

        assertNotNull(result);
    }

  @Test
  void testPasarSaldo_ok() {


    TarjetaVirtual origen = new TarjetaVirtual();
    TarjetaVirtual destino = new TarjetaVirtual();

    origen.setIdTarjeta(1L);
    destino.setIdTarjeta(2L);
    origen.setSaldo(BigDecimal.valueOf(1000));
    destino.setSaldo(BigDecimal.valueOf(500));

    origen.setEstado(EstadoTarjetaEnum.ACTIVA);
    destino.setEstado(EstadoTarjetaEnum.ACTIVA);

  
    Usuario usuarioOrigen = new Usuario();
    usuarioOrigen.setId(1L);
    usuarioOrigen.setCorreo("origen@test.com");

    Pasajero pasajeroOrigen = new Pasajero();
    pasajeroOrigen.setUsuario(usuarioOrigen);

    origen.setPasajero(pasajeroOrigen);

    Usuario usuarioDestino = new Usuario();
    usuarioDestino.setId(2L);
    usuarioDestino.setCorreo("destino@test.com");

    Pasajero pasajeroDestino = new Pasajero();
    pasajeroDestino.setUsuario(usuarioDestino);

    destino.setPasajero(pasajeroDestino);


    when(tarjetaRepository.findByPasajeroUsuarioId(1L))
            .thenReturn(Optional.of(origen));

    when(tarjetaRepository.findTarjetaVirtualByPasajeroUsuarioDatosPersonalesTelefono("123"))
            .thenReturn(Optional.of(destino));

    when(transaccionRepository.save(any(PasarSaldo.class)))
        .thenAnswer(invocation -> {
            PasarSaldo t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

    String result = service.PasarSaldo("123", BigDecimal.valueOf(100), 1L);

    assertEquals("Transferencia realizada exitosamente.", result);
    verify(tarjetaService).descontarSaldo(1L, BigDecimal.valueOf(100));
}
}