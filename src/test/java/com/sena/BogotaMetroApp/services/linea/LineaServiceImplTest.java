package com.sena.BogotaMetroApp.services.linea;

import com.sena.BogotaMetroApp.mapper.LineaMapper;
import com.sena.BogotaMetroApp.persistence.models.Linea;
import com.sena.BogotaMetroApp.persistence.repository.linea.LineaRepository;
import com.sena.BogotaMetroApp.presentation.dto.linea.LineaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.linea.LineaResponseDTO;

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
class LineaServicesImplTest {

    @Mock
    private LineaRepository lineaRepository;

    @Mock
    private LineaMapper lineaMapper;

    @InjectMocks
    private LineaServicesImpl service;

    private LineaRequestDTO requestDTO;
    private Linea linea;
    private LineaResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new LineaRequestDTO();
        requestDTO.setNombre("Línea 1");
        requestDTO.setColor("Rojo");
        requestDTO.setFrecuenciaMinutos(5);

        linea = new Linea();
        linea.setId(1L);
        linea.setActivo(true);

        responseDTO = new LineaResponseDTO();
    }

    @Test
    void testCrear() {

        when(lineaRepository.save(any(Linea.class))).thenReturn(linea);
        when(lineaMapper.toDTO(linea)).thenReturn(responseDTO);

        LineaResponseDTO result = service.crear(requestDTO);

        assertNotNull(result);
        verify(lineaRepository).save(any(Linea.class));
    }

    @Test
    void testObtener() {

        when(lineaRepository.findById(1L)).thenReturn(Optional.of(linea));
        when(lineaMapper.toDTO(linea)).thenReturn(responseDTO);

        LineaResponseDTO result = service.obtener(1L);

        assertNotNull(result);
    }

    @Test
    void testObtenerNoExiste() {

        when(lineaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.obtener(1L));
    }

    @Test
    void testObtenerInactivo() {

        linea.setActivo(false);
        when(lineaRepository.findById(1L)).thenReturn(Optional.of(linea));

        assertThrows(RuntimeException.class,
                () -> service.obtener(1L));
    }

    @Test
    void testListar() {

        when(lineaRepository.findByActivoTrue()).thenReturn(List.of(linea));
        when(lineaMapper.toDTO(linea)).thenReturn(responseDTO);

        List<LineaResponseDTO> result = service.listar();

        assertEquals(1, result.size());
        verify(lineaRepository).findByActivoTrue();
    }

    @Test
    void testActualizar() {

        when(lineaRepository.findById(1L)).thenReturn(Optional.of(linea));
        when(lineaRepository.save(linea)).thenReturn(linea);
        when(lineaMapper.toDTO(linea)).thenReturn(responseDTO);

        LineaResponseDTO result = service.actualizar(1L, requestDTO);

        assertNotNull(result);
        verify(lineaRepository).save(linea);
    }

    @Test
    void testActualizarNoExiste() {

        when(lineaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.actualizar(1L, requestDTO));
    }

    @Test
    void testEliminar() {

        when(lineaRepository.findById(1L)).thenReturn(Optional.of(linea));

        service.eliminar(1L);

        assertFalse(linea.isActivo());
        verify(lineaRepository).save(linea);
    }

    @Test
    void testEliminarNoExiste() {

        when(lineaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.eliminar(1L));
    }
}