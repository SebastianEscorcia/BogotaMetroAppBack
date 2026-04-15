package com.sena.BogotaMetroApp.services.interrupcion;

import com.sena.BogotaMetroApp.events.InterrupcionEvent;
import com.sena.BogotaMetroApp.mapper.InterrupcionMapper;
import com.sena.BogotaMetroApp.persistence.models.Linea;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import com.sena.BogotaMetroApp.persistence.models.interrupcion.Interrupcion;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionRepository;
import com.sena.BogotaMetroApp.persistence.repository.interrupcion.InterrupcionRepository;
import com.sena.BogotaMetroApp.persistence.repository.linea.LineaRepository;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionUpdateDTO;
import com.sena.BogotaMetroApp.services.exception.interrupcion.InterrupcionException;
import com.sena.BogotaMetroApp.utils.enums.EstadoInterrupcionEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterrupcionServicesImplTest {

    @Mock
    private InterrupcionRepository repository;

    @Mock
    private EstacionRepository estacionRepository;

    @Mock
    private LineaRepository lineaRepository;

    @Mock
    private InterrupcionMapper mapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private InterrupcionServicesImpl service;

    private InterrupcionRequestDTO requestDTO;
    private InterrupcionUpdateDTO updateDTO;
    private Interrupcion interrupcion;
    private InterrupcionResponseDTO responseDTO;
    private Estacion estacion;
    private Linea linea;

    @BeforeEach
    void setUp() {
        requestDTO = new InterrupcionRequestDTO();
        requestDTO.setIdEstacion(1L);
        requestDTO.setDescripcion("Falla técnica");
        requestDTO.setInicio(LocalDateTime.now());

        updateDTO = new InterrupcionUpdateDTO();
        updateDTO.setDescripcion("Actualizada");

        estacion = new Estacion();
        estacion.setId(1L);

        linea = new Linea();
        linea.setId(1L);

        interrupcion = new Interrupcion();
        interrupcion.setId(1L);
        interrupcion.setActivo(true);
        interrupcion.setEstado(EstadoInterrupcionEnum.ACTIVA);

        responseDTO = new InterrupcionResponseDTO();
    }

    @Test
    void testCrear() {

        when(estacionRepository.findById(1L)).thenReturn(Optional.of(estacion));
        when(repository.save(any(Interrupcion.class))).thenReturn(interrupcion);
        when(mapper.toDTO(interrupcion)).thenReturn(responseDTO);

        InterrupcionResponseDTO result = service.crear(requestDTO);

        assertNotNull(result);
        verify(repository).save(any(Interrupcion.class));
        verify(eventPublisher).publishEvent(any(InterrupcionEvent.class));
    }

    @Test
    void testCrearSinDatos() {
        requestDTO.setIdEstacion(null);

        assertThrows(InterrupcionException.class,
                () -> service.crear(requestDTO));
    }

    @Test
    void testListar() {

        when(repository.findByActivoTrue()).thenReturn(List.of(interrupcion));
        when(mapper.toDTO(interrupcion)).thenReturn(responseDTO);

        List<InterrupcionResponseDTO> result = service.listar();

        assertEquals(1, result.size());
    }

    @Test
    void testEliminar() {

        when(repository.findById(1L)).thenReturn(Optional.of(interrupcion));

        service.eliminar(1L);

        assertFalse(interrupcion.getActivo() == false ? false : true); // o simplemente verificar save
        verify(repository).save(interrupcion);
        verify(eventPublisher).publishEvent(any(InterrupcionEvent.class));
    }

    @Test
    void testEliminarNoExiste() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InterrupcionException.class,
                () -> service.eliminar(1L));
    }

    @Test
    void testMarcarComoSolucionada() {

        when(repository.findById(1L)).thenReturn(Optional.of(interrupcion));
        when(repository.save(interrupcion)).thenReturn(interrupcion);
        when(mapper.toDTO(interrupcion)).thenReturn(responseDTO);

        service.marcarComoSolucionada(1L);

        assertEquals(EstadoInterrupcionEnum.SOLUCIONADA, interrupcion.getEstado());
        verify(eventPublisher).publishEvent(any(InterrupcionEvent.class));
    }

    @Test
    void testMarcarComoSolucionadaEliminada() {

        interrupcion.setActivo(false);
        when(repository.findById(1L)).thenReturn(Optional.of(interrupcion));

        assertThrows(InterrupcionException.class,
                () -> service.marcarComoSolucionada(1L));
    }

    @Test
    void testActualizar() {

        when(repository.findById(1L)).thenReturn(Optional.of(interrupcion));
        when(repository.save(interrupcion)).thenReturn(interrupcion);
        when(mapper.toDTO(interrupcion)).thenReturn(responseDTO);

        InterrupcionResponseDTO result = service.actualizar(1L, updateDTO);

        assertNotNull(result);
        verify(eventPublisher).publishEvent(any(InterrupcionEvent.class));
    }

    @Test
    void testActualizarNoExiste() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InterrupcionException.class,
                () -> service.actualizar(1L, updateDTO));
    }

    @Test
    void testActualizarSolucionada() {

        interrupcion.setEstado(EstadoInterrupcionEnum.SOLUCIONADA);
        when(repository.findById(1L)).thenReturn(Optional.of(interrupcion));

        assertThrows(InterrupcionException.class,
                () -> service.actualizar(1L, updateDTO));
    }

    @Test
    void testTieneInterrupcionActiva() {

        when(repository.existsByEstacionIdAndActivoTrueAndEstado(1L, EstadoInterrupcionEnum.ACTIVA))
                .thenReturn(true);

        boolean result = service.tieneInterrupcionActiva(1L);

        assertTrue(result);
    }
}