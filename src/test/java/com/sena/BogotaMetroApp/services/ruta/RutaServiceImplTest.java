package com.sena.BogotaMetroApp.services.ruta;

import com.sena.BogotaMetroApp.mapper.RutaMapper;
import com.sena.BogotaMetroApp.persistence.models.Ruta;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import com.sena.BogotaMetroApp.persistence.repository.RutaRepository;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionRepository;
import com.sena.BogotaMetroApp.presentation.dto.ruta.RutaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.ruta.RutaResponseDTO;
import com.sena.BogotaMetroApp.services.exception.ruta.RutaException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RutaServicesImplTest {

    @Mock
    private RutaRepository rutaRepository;

    @Mock
    private EstacionRepository estacionRepository;

    @Mock
    private RutaMapper rutaMapper;

    @InjectMocks
    private RutaServicesImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrear_ok() {
        RutaRequestDTO dto = new RutaRequestDTO();
        dto.setEstacionInicioId(1L);
        dto.setEstacionFinId(2L);
 
        Estacion inicio = new Estacion();
        Estacion fin = new Estacion();
        RutaResponseDTO response = new RutaResponseDTO();

        when(estacionRepository.findById(1L)).thenReturn(Optional.of(inicio));
        when(estacionRepository.findById(2L)).thenReturn(Optional.of(fin));
        when(rutaRepository.save(any(Ruta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(rutaMapper.toDTO(any(Ruta.class))).thenReturn(response);

        RutaResponseDTO result = service.crear(dto);

        assertNotNull(result);
    }

    @Test
    void testCrear_estacionesNull() {
        RutaRequestDTO dto = new RutaRequestDTO();

        assertThrows(RutaException.class, () -> {
            service.crear(dto);
        });
    }

    @Test
    void testCrear_estacionNoExiste() {
        RutaRequestDTO dto = new RutaRequestDTO();
        dto.setEstacionInicioId(1L);
        dto.setEstacionFinId(2L);

        when(estacionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RutaException.class, () -> {
            service.crear(dto);
        });
    }

    @Test
    void testObtener_ok() {
        Ruta ruta = new Ruta();
        ruta.setActivo(true);

        RutaResponseDTO response = new RutaResponseDTO();

        when(rutaRepository.findById(1L)).thenReturn(Optional.of(ruta));
        when(rutaMapper.toDTO(ruta)).thenReturn(response);

        RutaResponseDTO result = service.obtener(1L);

        assertNotNull(result);
    }

    @Test
    void testObtener_noExiste() {
        when(rutaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RutaException.class, () -> {
            service.obtener(1L);
        });
    }

    @Test
    void testObtener_inactiva() {
        Ruta ruta = new Ruta();
        ruta.setActivo(false);

        when(rutaRepository.findById(1L)).thenReturn(Optional.of(ruta));

        assertThrows(RutaException.class, () -> {
            service.obtener(1L);
        });
    }

    @Test
    void testListar_ok() {
        Ruta ruta = new Ruta();
        RutaResponseDTO response = new RutaResponseDTO();

        when(rutaRepository.findByActivoTrue()).thenReturn(List.of(ruta));
        when(rutaMapper.toDTO(ruta)).thenReturn(response);

        List<RutaResponseDTO> result = service.listar();

        assertEquals(1, result.size());
    }

    @Test
    void testActualizar_ok() {
        Ruta ruta = new Ruta();
        RutaRequestDTO dto = new RutaRequestDTO();
        dto.setEstacionInicioId(1L);
        dto.setEstacionFinId(2L);

        Estacion inicio = new Estacion();
        Estacion fin = new Estacion();
        RutaResponseDTO response = new RutaResponseDTO();

        when(rutaRepository.findById(1L)).thenReturn(Optional.of(ruta));
        when(estacionRepository.findById(1L)).thenReturn(Optional.of(inicio));
        when(estacionRepository.findById(2L)).thenReturn(Optional.of(fin));
        when(rutaRepository.save(any(Ruta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(rutaMapper.toDTO(ruta)).thenReturn(response);

        RutaResponseDTO result = service.actualizar(1L, dto);

        assertNotNull(result);
    }

    @Test
    void testActualizar_noExiste() {
        when(rutaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RutaException.class, () -> {
            service.actualizar(1L, new RutaRequestDTO());
        });
    }

    @Test
    void testEliminar_ok() {
        Ruta ruta = new Ruta();

        when(rutaRepository.findById(1L)).thenReturn(Optional.of(ruta));

        service.eliminar(1L);

        assertFalse(ruta.isActivo());
        verify(rutaRepository).save(ruta);
    }

    @Test
    void testEliminar_noExiste() {
        when(rutaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RutaException.class, () -> {
            service.eliminar(1L);
        });
    }
}