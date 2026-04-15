package com.sena.BogotaMetroApp.services.operador;

import com.sena.BogotaMetroApp.mapper.operador.OperadorMapper;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.operador.Operador;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.persistence.repository.operador.OperadorRepository;
import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.operador.RegistroOperadorDTO;
import com.sena.BogotaMetroApp.services.factory.DatosPersonalesFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperadorServiceImplTest {

    @Mock
    private OperadorRepository operadorRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private DatosPersonalesFactory datosPersonalesFactory;

    @Mock
    private OperadorMapper mapper;

    @InjectMocks
    private OperadorServiceImpl operadorService;

    private Operador operador;
    private Usuario usuario;
    private RegistroOperadorDTO dto;
    private OperadorResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setActivo(true);
        usuario.setActivo(true);

        operador = new Operador();
        operador.setUsuario(usuario);

        dto = new RegistroOperadorDTO();

        responseDTO = new OperadorResponseDTO();
    }

    @Test
    void testRegistrar() {
        when(mapper.toEntity(dto)).thenReturn(operador);
        when(mapper.toDTO(operador)).thenReturn(responseDTO);

        OperadorResponseDTO result = operadorService.registrar(dto);

        assertNotNull(result);
        verify(usuarioRepository).save(usuario);
        verify(operadorRepository).save(operador);
        verify(mapper).toDTO(operador);
    }

    @Test
    void testListarOperadores() {
        when(operadorRepository.buscarPorFiltro("test")).thenReturn(List.of(operador));
        when(mapper.toDTO(operador)).thenReturn(responseDTO);

        List<OperadorResponseDTO> result = operadorService.listarOperadores("test");

        assertEquals(1, result.size());
        verify(operadorRepository).buscarPorFiltro("test");
    }

    @Test
    void testActualizar() {
        when(operadorRepository.findById(1L)).thenReturn(Optional.of(operador));
        when(operadorRepository.save(operador)).thenReturn(operador);
        when(mapper.toDTO(operador)).thenReturn(responseDTO);

        OperadorResponseDTO result = operadorService.actualizar(1L, dto);

        assertNotNull(result);
        verify(datosPersonalesFactory).actualizarDatos(usuario, dto);
        verify(operadorRepository).save(operador);
    }

    @Test
    void testActualizarOperadorNoExiste() {
        when(operadorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> operadorService.actualizar(1L, dto));
    }

    @Test
    void testEliminar() {
        when(operadorRepository.findById(1L)).thenReturn(Optional.of(operador));

        operadorService.eliminar(1L);

        assertFalse(usuario.isActivo());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testEliminarYaDeshabilitado() {
        usuario.setActivo(false);
        when(operadorRepository.findById(1L)).thenReturn(Optional.of(operador));

        assertThrows(RuntimeException.class,
                () -> operadorService.eliminar(1L));
    }

    @Test
    void testObtenerPorId() {
        when(operadorRepository.findById(1L)).thenReturn(Optional.of(operador));
        when(mapper.toDTO(operador)).thenReturn(responseDTO);

        OperadorResponseDTO result = operadorService.obtenerPorId(1L);

        assertNotNull(result);
    }

    @Test
    void testObtenerPorIdNoExiste() {
        when(operadorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> operadorService.obtenerPorId(1L));
    }

    @Test
    void testObtenerPorCorreo() {
        when(operadorRepository.findByUsuarioCorreo("test@mail.com"))
                .thenReturn(Optional.of(operador));
        when(mapper.toDTO(operador)).thenReturn(responseDTO);

        OperadorResponseDTO result = operadorService.obtenerPorCorreo("test@mail.com");

        assertNotNull(result);
    }

    @Test
    void testObtenerPorCorreoInactivo() {
        usuario.setActivo(false);

        when(operadorRepository.findByUsuarioCorreo("test@mail.com"))
                .thenReturn(Optional.of(operador));

        assertThrows(RuntimeException.class,
                () -> operadorService.obtenerPorCorreo("test@mail.com"));
    }

    @Test
    void testReactivar() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        operadorService.reactivar(1L);

        assertTrue(usuario.isActivo());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testReactivarUsuarioNoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> operadorService.reactivar(1L));
    }
}
