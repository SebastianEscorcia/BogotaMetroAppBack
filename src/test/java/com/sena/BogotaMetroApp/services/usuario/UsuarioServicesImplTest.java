package com.sena.BogotaMetroApp.services.usuario;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sena.BogotaMetroApp.mapper.UsuarioMapper;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.presentation.dto.usuario.UsuarioRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.usuario.UsuarioResponseDTO;
import com.sena.BogotaMetroApp.services.factory.UsuarioFactory;

public class UsuarioServicesImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper mapper;

    @Mock
    private UsuarioFactory usuarioFactory;

    @InjectMocks
    private UsuarioServicesImpl usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearUsuario() {

        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setCorreo("diana@test.com");
        dto.setClave("1234");
        dto.setIdRol(1L);

        when(usuarioFactory.crearAdminDesdeRegistro(any(), any()))
                .thenReturn(new Usuario());

        when(usuarioRepository.save(any()))
                .thenReturn(new Usuario());

        when(mapper.toDTO(any()))
                .thenReturn(new UsuarioResponseDTO());

        UsuarioResponseDTO resultado = usuarioService.crearUsuario(dto);

        assertNotNull(resultado);

        // 🔥 importante
        verify(usuarioRepository).save(any());
    }
}