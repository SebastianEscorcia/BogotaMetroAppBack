package com.sena.BogotaMetroApp.services.conexion;

import com.sena.BogotaMetroApp.mapper.conexion.ConexionMapper;
import com.sena.BogotaMetroApp.persistence.models.conexion.Conexion;
import com.sena.BogotaMetroApp.persistence.repository.conexion.ConexionRepository;
import com.sena.BogotaMetroApp.presentation.dto.conexion.ConexionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.conexion.ConexionResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConexionServicesImplTest {

    @Mock
    private ConexionRepository conexionRepository;

    @Mock
    private ConexionMapper mapper;

    @InjectMocks
    private ConexionServicesImpl conexionService;

    private ConexionRequestDTO requestDTO;
    private Conexion conexion;
    private ConexionResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new ConexionRequestDTO();
        requestDTO.setIdOrigen(1L);
        requestDTO.setIdDestino(2L);

        conexion = new Conexion();
        responseDTO = new ConexionResponseDTO();
    }

    @Test
    void testCrear() {

        when(mapper.toEntity(requestDTO)).thenReturn(conexion);
        when(conexionRepository.save(conexion)).thenReturn(conexion);
        when(mapper.toDTO(conexion)).thenReturn(responseDTO);

        ConexionResponseDTO result = conexionService.crear(requestDTO);

        assertNotNull(result);
        verify(conexionRepository).save(conexion);
        verify(mapper).toDTO(conexion);
    }

    @Test
    void testCrearErrorIdsInvalidos() {
        requestDTO.setIdOrigen(2L);
        requestDTO.setIdDestino(1L);

        assertThrows(IllegalArgumentException.class,
                () -> conexionService.crear(requestDTO));
    }

    @Test
    void testListar() {

        when(conexionRepository.findAll()).thenReturn(List.of(conexion));
        when(mapper.toDTO(conexion)).thenReturn(responseDTO);

        List<ConexionResponseDTO> result = conexionService.listar();

        assertEquals(1, result.size());
        verify(conexionRepository).findAll();
    }
}