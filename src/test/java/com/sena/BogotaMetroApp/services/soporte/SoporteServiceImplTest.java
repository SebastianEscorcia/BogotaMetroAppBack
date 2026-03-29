package com.sena.BogotaMetroApp.services.soporte;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sena.BogotaMetroApp.mapper.soporte.SoporteMapper;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.soporte.Soporte;
import com.sena.BogotaMetroApp.persistence.repository.RoleRepository;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.persistence.repository.soporte.SoporteRepository;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteResponseDTO;
import com.sena.BogotaMetroApp.services.factory.DatosPersonalesFactory;
import com.sena.BogotaMetroApp.persistence.models.Role;




public class SoporteServiceImplTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private SoporteRepository soporteRepository;

    @Mock
    private DatosPersonalesFactory datosPersonalesFactory;

    @Mock
    private SoporteMapper mapper;

    @InjectMocks
    private SoporteServiceImpl soporteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarSoporte() {

        SoporteRequestDTO dto = new SoporteRequestDTO();
        dto.setCorreo("soporte@test.com");
        dto.setClave("1234");

        // objetos simulados
        Role role = new Role();
        Soporte soporte = new Soporte();
        Usuario usuario = new Usuario();
        soporte.setUsuario(usuario);

        SoporteResponseDTO response = new SoporteResponseDTO();

        when(usuarioRepository.existsByCorreo(dto.getCorreo()))
                .thenReturn(false);

        when(roleRepository.findByNombre(any()))
                .thenReturn(Optional.of(role));

        when(mapper.toEntity(dto, role))
                .thenReturn(soporte);

        when(mapper.toDTO(soporte))
                .thenReturn(response);

        SoporteResponseDTO resultado = soporteService.registrar(dto);

        // validaciones
        assertNotNull(resultado);
        verify(soporteRepository).save(soporte);
        verify(usuarioRepository).save(usuario);
    }
}
