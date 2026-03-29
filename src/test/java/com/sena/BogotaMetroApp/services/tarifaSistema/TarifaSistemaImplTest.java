package com.sena.BogotaMetroApp.services.tarifaSistema;

import com.sena.BogotaMetroApp.mapper.tarifasistema.TarifaSistemaMapper;
import com.sena.BogotaMetroApp.persistence.models.tarifasistema.TarifaSistema;
import com.sena.BogotaMetroApp.persistence.repository.tarifasistema.TarifaSistemaRepository;
import com.sena.BogotaMetroApp.presentation.dto.tarifasistema.TarifaSistemaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.tarifasistema.TarifaSistemaResponseDTO;
import com.sena.BogotaMetroApp.services.exception.tarifasistema.TarifaSistemaException;
import com.sena.BogotaMetroApp.services.tarifasistema.TarifaSistemaImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TarifaSistemaImplTest {

    @Mock
    private TarifaSistemaRepository repository;

    @Mock
    private TarifaSistemaMapper mapper;

    @InjectMocks
    private TarifaSistemaImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearTarifa_ok() {
        TarifaSistemaRequestDTO request = new TarifaSistemaRequestDTO();
        TarifaSistema tarifa = new TarifaSistema();
        TarifaSistemaResponseDTO response = new TarifaSistemaResponseDTO();

        when(repository.findByActivaTrue()).thenReturn(Optional.empty());
        when(mapper.toEntity(request)).thenReturn(tarifa);
        when(repository.save(any(TarifaSistema.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDTO(tarifa)).thenReturn(response);

        TarifaSistemaResponseDTO result = service.crearTarifa(request);

        assertNotNull(result);
        verify(repository).save(any(TarifaSistema.class));
    }

    @Test
    void testObtenerTarifaActiva_ok() {
        TarifaSistema tarifa = new TarifaSistema();
        TarifaSistemaResponseDTO response = new TarifaSistemaResponseDTO();

        when(repository.findByActivaTrue()).thenReturn(Optional.of(tarifa));
        when(mapper.toDTO(tarifa)).thenReturn(response);

        TarifaSistemaResponseDTO result = service.obtenerTarifaActiva();

        assertNotNull(result);
    }

    @Test
    void testObtenerTarifaActiva_noExiste() {
        when(repository.findByActivaTrue()).thenReturn(Optional.empty());

        assertThrows(TarifaSistemaException.class, () -> {
            service.obtenerTarifaActiva();
        });
    }

   
    @Test
    void testActualizarTarifa_ok() {
        TarifaSistema tarifa = new TarifaSistema();
        TarifaSistemaRequestDTO request = new TarifaSistemaRequestDTO();
        TarifaSistemaResponseDTO response = new TarifaSistemaResponseDTO();

        when(repository.findById(1L)).thenReturn(Optional.of(tarifa));
        when(repository.save(any(TarifaSistema.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDTO(tarifa)).thenReturn(response);

        TarifaSistemaResponseDTO result = service.actualizarTarifa(1L, request);

        assertNotNull(result);
    }

    @Test
    void testActualizarTarifa_noExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TarifaSistemaException.class, () -> {
            service.actualizarTarifa(1L, new TarifaSistemaRequestDTO());
        });
    }

    @Test
    void testEliminarTarifa_ok() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminarTarifa(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void testEliminarTarifa_noExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(TarifaSistemaException.class, () -> {
            service.eliminarTarifa(1L);
        });
    }

    @Test
    void testObtenerValorTarifaActual_ok() {
        TarifaSistema tarifa = new TarifaSistema();
        tarifa.setValorTarifa(BigDecimal.valueOf(3000));

        when(repository.findByActivaTrue()).thenReturn(Optional.of(tarifa));

        BigDecimal result = service.obtenerValorTarifaActual();

        assertEquals(BigDecimal.valueOf(3000), result);
    }

    @Test
    void testObtenerValorTarifaActual_noExiste() {
        when(repository.findByActivaTrue()).thenReturn(Optional.empty());

        assertThrows(TarifaSistemaException.class, () -> {
            service.obtenerValorTarifaActual();
        });
    }
}