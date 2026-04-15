package com.sena.BogotaMetroApp.services.puntointeres;

import com.sena.BogotaMetroApp.mapper.puntointeres.PuntoInteresMapper;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import com.sena.BogotaMetroApp.persistence.models.puntointeres.PuntoInteres;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionRepository;
import com.sena.BogotaMetroApp.persistence.repository.puntointeres.PuntoInteresRepository;
import com.sena.BogotaMetroApp.presentation.dto.puntointeres.PuntoInteresRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.puntointeres.PuntoInteresResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PuntoInteresServicesImplTest {

    @Mock
    private PuntoInteresRepository poiRepository;

    @Mock
    private PuntoInteresMapper mapper;

    @Mock
    private EstacionRepository estacionRepository;

    @InjectMocks
    private PuntoInteresServicesImpl service;

    private PuntoInteresRequestDTO requestDTO;
    private PuntoInteres punto;
    private PuntoInteresResponseDTO responseDTO;
    private Estacion estacion;

    @BeforeEach
    void setUp() {
        requestDTO = new PuntoInteresRequestDTO();
        requestDTO.setNombre("Parque");
        requestDTO.setCategoria("Turismo");
        requestDTO.setLatitud(BigDecimal.valueOf(4.6));
        requestDTO.setLongitud(BigDecimal.valueOf(-74.1));
        requestDTO.setIdEstacion(1L);


        estacion = new Estacion();
        estacion.setId(1L);
        estacion.setActivo(true);

        punto = new PuntoInteres();
        punto.setId(1L);
        punto.setActivo(true);
        punto.setEstacion(estacion);

        responseDTO = new PuntoInteresResponseDTO();
    }

    @Test
    void testCrear() {
        when(mapper.toEntity(requestDTO)).thenReturn(punto);
        when(poiRepository.save(punto)).thenReturn(punto);
        when(mapper.toDTO(punto)).thenReturn(responseDTO);

        PuntoInteresResponseDTO result = service.crear(requestDTO);

        assertNotNull(result);
        verify(poiRepository).save(punto);
    }

    @Test
    void testListar() {
        when(poiRepository.findByActivoTrue()).thenReturn(List.of(punto));
        when(mapper.toDTO(punto)).thenReturn(responseDTO);

        List<PuntoInteresResponseDTO> result = service.listar();

        assertEquals(1, result.size());
        verify(poiRepository).findByActivoTrue();
    }

    @Test
    void testActualizar() {
        
        requestDTO.setIdEstacion(1L);
        
        when(poiRepository.findById(1L)).thenReturn(Optional.of(punto));
        when(poiRepository.save(punto)).thenReturn(punto);
        when(mapper.toDTO(punto)).thenReturn(responseDTO);
        
        PuntoInteresResponseDTO result = service.actualizar(1L, requestDTO);
        
        assertNotNull(result);
        verify(poiRepository).save(punto);
        
        verify(estacionRepository, never()).findById(any());
    }
     
    @Test
    void testActualizarNoExiste() {
        when(poiRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.actualizar(1L, requestDTO));
    }

    @Test
    void testActualizarInactivo() {
        punto.setActivo(false);
        when(poiRepository.findById(1L)).thenReturn(Optional.of(punto));

        assertThrows(RuntimeException.class,
                () -> service.actualizar(1L, requestDTO));
    }

    @Test
    void testActualizarEstacionNoExiste() {
        requestDTO.setIdEstacion(2L);

        when(poiRepository.findById(1L)).thenReturn(Optional.of(punto));
        when(estacionRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.actualizar(1L, requestDTO));
    }

    @Test
    void testActualizarEstacionInactiva() {
        requestDTO.setIdEstacion(2L);

        Estacion nueva = new Estacion();
        nueva.setId(2L);
        nueva.setActivo(false);

        when(poiRepository.findById(1L)).thenReturn(Optional.of(punto));
        when(estacionRepository.findById(2L)).thenReturn(Optional.of(nueva));

        assertThrows(RuntimeException.class,
                () -> service.actualizar(1L, requestDTO));
    }

    @Test
    void testEliminar() {
        when(poiRepository.findById(1L)).thenReturn(Optional.of(punto));

        service.eliminar(1L);

        assertFalse(punto.isActivo());
        verify(poiRepository).save(punto);
    }

    @Test
    void testEliminarNoExiste() {
        when(poiRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.eliminar(1L));
    }
}