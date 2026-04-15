package com.sena.BogotaMetroApp.services.pasajero;

import com.sena.BogotaMetroApp.mapper.pasajero.PasajeroMapper;
import com.sena.BogotaMetroApp.persistence.models.DatosPersonales;
import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.persistence.repository.pasajero.PasajeroRepository;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroUpdateDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.RegistroPasajeroUnificadoDTO;
import com.sena.BogotaMetroApp.services.exception.pasajero.PasajeroException;
import com.sena.BogotaMetroApp.services.factory.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasajeroServiceImplTest {

    @Mock
    private PasajeroRepository pasajeroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioFactory usuarioFactory;

    @Mock
    private DatosPersonalesFactory datosPersonalesFactory;

    @Mock
    private PasajeroFactory pasajeroFactory;

    @Mock
    private TarjetaVirtualFactory tarjetaVirtualFactory;

    @Mock
    private PasajeroMapper mapper;

    @InjectMocks
    private PasajeroServiceImpl pasajeroService;

    private RegistroPasajeroUnificadoDTO dto;
    private Usuario usuario;
    private Pasajero pasajero;
    private PasajeroResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        dto = new RegistroPasajeroUnificadoDTO();
        dto.setCorreo("test@mail.com");
        dto.setNumDocumento("123");
        dto.setTelefono("3001234567");

        usuario = new Usuario();
        usuario.setActivo(true);

        pasajero = new Pasajero();
        pasajero.setUsuario(usuario);

        responseDTO = new PasajeroResponseDTO();
    }

    @Test
    void testRegistrarConUsuario() {

        when(usuarioRepository.findByCorreo(dto.getCorreo())).thenReturn(Optional.empty());
        when(usuarioRepository.findByDatosPersonales_NumDocumento(dto.getNumDocumento())).thenReturn(Optional.empty());
        when(usuarioRepository.findByDatosPersonales_Telefono(dto.getTelefono())).thenReturn(Optional.empty());

        when(usuarioFactory.crearDesdeRegistro(dto, "PASAJERO")).thenReturn(usuario);

        DatosPersonales dp = new DatosPersonales();
        when(datosPersonalesFactory.crearDesdeRegistro(dto, usuario)).thenReturn(dp);

        when(pasajeroFactory.crear(usuario)).thenReturn(pasajero);

        TarjetaVirtual tarjeta = new TarjetaVirtual();
        when(tarjetaVirtualFactory.crearTarjetaVirtual(pasajero)).thenReturn(tarjeta);

        when(mapper.toDTO(pasajero)).thenReturn(responseDTO);

        PasajeroResponseDTO result = pasajeroService.registrarConUsuario(dto);

        assertNotNull(result);
        verify(pasajeroRepository).save(pasajero);
    }

    @Test
    void testRegistrarUsuarioYaExistePorCorreo() {
        when(usuarioRepository.findByCorreo(dto.getCorreo())).thenReturn(Optional.of(usuario));

        assertThrows(PasajeroException.class,
                () -> pasajeroService.registrarConUsuario(dto));
    }

    @Test
    void testObtenerPorCorreo() {
        when(pasajeroRepository.findByUsuarioCorreo("test@mail.com"))
                .thenReturn(Optional.of(pasajero));
        when(mapper.toDTO(pasajero)).thenReturn(responseDTO);

        PasajeroResponseDTO result = pasajeroService.obtenerPorCorreo("test@mail.com");

        assertNotNull(result);
    }

    @Test
    void testObtenerPorCorreoInactivo() {
        usuario.setActivo(false);

        when(pasajeroRepository.findByUsuarioCorreo("test@mail.com"))
                .thenReturn(Optional.of(pasajero));

        assertThrows(PasajeroException.class,
                () -> pasajeroService.obtenerPorCorreo("test@mail.com"));
    }

    @Test
    void testObtenerPorCorreoNoExiste() {
        when(pasajeroRepository.findByUsuarioCorreo("test@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> pasajeroService.obtenerPorCorreo("test@mail.com"));
    }

    @Test
    void testObtenerPorId() {
        when(pasajeroRepository.findById(1L)).thenReturn(Optional.of(pasajero));
        when(mapper.toDTO(pasajero)).thenReturn(responseDTO);

        PasajeroResponseDTO result = pasajeroService.obtener(1L);

        assertNotNull(result);
    }

    @Test
    void testObtenerPorIdNoExiste() {
        when(pasajeroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PasajeroException.class,
                () -> pasajeroService.obtener(1L));
    }

    @Test
    void testListarTodos() {
        when(pasajeroRepository.findAll()).thenReturn(List.of(pasajero));
        when(mapper.toDTO(pasajero)).thenReturn(responseDTO);

        List<PasajeroResponseDTO> result = pasajeroService.listarTodos();

        assertEquals(1, result.size());
    }

    @Test
    void testEliminar() {
        when(pasajeroRepository.findById(1L)).thenReturn(Optional.of(pasajero));

        pasajeroService.eliminar(1L);

        verify(pasajeroRepository).delete(pasajero);
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void testEliminarNoExiste() {
        when(pasajeroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PasajeroException.class,
                () -> pasajeroService.eliminar(1L));
    }

    @Test
    void testActualizar() {

    PasajeroUpdateDTO updateDTO = new PasajeroUpdateDTO(
        "Juan",
        "3001234567",
        java.time.LocalDate.of(2000, 1, 1)
    );

    Pasajero pasajeroSpy = spy(new Pasajero());
    pasajeroSpy.setUsuario(usuario);

    when(pasajeroRepository.findByUsuarioCorreo("test@mail.com"))
            .thenReturn(Optional.of(pasajeroSpy));

    doNothing().when(pasajeroSpy).actualizarDatosPersonales(any());

    when(mapper.toDTO(pasajeroSpy)).thenReturn(responseDTO);

    PasajeroResponseDTO result = pasajeroService.actualizar("test@mail.com", updateDTO);

    assertNotNull(result);
    verify(pasajeroRepository).save(pasajeroSpy);
}

    @Test
    void testActualizarNoExiste() {
        PasajeroUpdateDTO updateDTO = new PasajeroUpdateDTO(
            "Juan",
            "3001234567",
            java.time.LocalDate.of(2000, 1, 1)
        );

        when(pasajeroRepository.findByUsuarioCorreo("test@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(PasajeroException.class,
                () -> pasajeroService.actualizar("test@mail.com", updateDTO));
    }
}