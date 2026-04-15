package com.sena.BogotaMetroApp.services.estacion;

import com.sena.BogotaMetroApp.mapper.EstacionMapper;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionRepository;
import com.sena.BogotaMetroApp.presentation.dto.estacion.EstacionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.estacion.EstacionResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EstacionServicesImplTest {

    @Mock
    private EstacionRepository estacionRepository;

    @Mock
    private EstacionMapper estacionMapper;

    @InjectMocks
    private EstacionServicesImpl estacionService;

    private Estacion estacion;
    private EstacionRequestDTO requestDTO;
    private EstacionResponseDTO responseDTO;


    
    @BeforeEach
    
    void setUp() {
        estacion = new Estacion();
        estacion.setId(1L);
        estacion.setNombre("Portal Norte");
        estacion.setEsAccesible(true);
        estacion.setTipo("Troncal");
        
        requestDTO = mock(EstacionRequestDTO.class);
        
        when(requestDTO.getNombre()).thenReturn("Portal Norte");
        when(requestDTO.getLatitud()).thenReturn(new BigDecimal("4.75"));
        when(requestDTO.getLongitud()).thenReturn(new BigDecimal("-74.05"));
        when(requestDTO.getEsAccesible()).thenReturn(true);
        when(requestDTO.getTipo()).thenReturn("Troncal");
        
        responseDTO = new EstacionResponseDTO();
        responseDTO.setNombre("Portal Norte");
        responseDTO.setEsAccesible(true);
        responseDTO.setTipo("Troncal");
}

    @Test
    void testCrearEstacion() {
        when(estacionRepository.save(any(Estacion.class))).thenReturn(estacion);
        when(estacionMapper.toDTO(any(Estacion.class))).thenReturn(responseDTO);

        EstacionResponseDTO resultado = estacionService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Portal Norte", resultado.getNombre());

        verify(estacionRepository).save(any(Estacion.class));
        verify(estacionMapper).toDTO(any(Estacion.class));
    }

    @Test
    void testObtenerEstacion() {
        when(estacionRepository.findById(1L)).thenReturn(Optional.of(estacion));
        when(estacionMapper.toDTO(estacion)).thenReturn(responseDTO);

        EstacionResponseDTO resultado = estacionService.obtener(1L);

        assertNotNull(resultado);
        assertEquals("Portal Norte", resultado.getNombre());

        verify(estacionRepository).findById(1L);
        verify(estacionMapper).toDTO(estacion);
    }

    @Test
    void testObtenerEstacionNoExiste() {
        when(estacionRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            estacionService.obtener(1L);
        });

        assertEquals("Estación no encontrada", ex.getMessage());

        verify(estacionRepository).findById(1L);
    }

    @Test
    void testListarEstaciones() {
        when(estacionRepository.findAll()).thenReturn(List.of(estacion));
        when(estacionMapper.toDTO(any(Estacion.class))).thenReturn(responseDTO);

        List<EstacionResponseDTO> lista = estacionService.listar();

        assertNotNull(lista);
        assertEquals(1, lista.size());

        verify(estacionRepository).findAll();
        verify(estacionMapper).toDTO(any(Estacion.class));
    }

    @Test
    void testActualizarEstacion() {
        when(estacionRepository.findById(1L)).thenReturn(Optional.of(estacion));
        when(estacionRepository.save(any(Estacion.class))).thenReturn(estacion);
        when(estacionMapper.toDTO(estacion)).thenReturn(responseDTO);

        EstacionResponseDTO resultado = estacionService.actualizar(1L, requestDTO);

        assertNotNull(resultado);
        assertEquals("Portal Norte", resultado.getNombre());

        verify(estacionRepository).findById(1L);
        verify(estacionRepository).save(estacion);
        verify(estacionMapper).toDTO(estacion);
    }

    @Test
    void testActualizarNoExiste() {
        when(estacionRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            estacionService.actualizar(1L, requestDTO);
        });

        assertEquals("Estación no encontrada", ex.getMessage());

        verify(estacionRepository).findById(1L);
    }

    @Test
    void testEliminarEstacion() {
        doNothing().when(estacionRepository).deleteById(1L);

        estacionService.eliminar(1L);

        verify(estacionRepository).deleteById(1L);
    }
}