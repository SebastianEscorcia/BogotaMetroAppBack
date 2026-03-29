package com.sena.BogotaMetroApp.services.horariosistema;

import com.sena.BogotaMetroApp.persistence.models.horariosistema.HorarioSistema;
import com.sena.BogotaMetroApp.persistence.repository.horariosistema.HorarioSistemaRepository;
import com.sena.BogotaMetroApp.presentation.dto.horariosistema.HorarioSistemaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.horariosistema.HorarioSistemaResponseDTO;
import com.sena.BogotaMetroApp.mapper.horariosistema.HorarioSistemaMapper;
import com.sena.BogotaMetroApp.utils.enums.DiaSemana;
import com.sena.BogotaMetroApp.utils.logic.HolidayService;
import com.sena.BogotaMetroApp.services.exception.horariosistema.HorarioSistemaException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HorarioSistemaServiceImplTest {
    

    @Mock
    private HorarioSistemaRepository horarioRepo;

    @Mock
    private HorarioSistemaMapper mapper;

        @Mock
    private HolidayService holidayService;

    @InjectMocks
    private HorarioSistemaServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearHorario_ok() {
        HorarioSistemaRequestDTO request = new HorarioSistemaRequestDTO();
        request.setDia(DiaSemana.LUNES);

        HorarioSistema horario = new HorarioSistema();
        HorarioSistemaResponseDTO response = new HorarioSistemaResponseDTO();

        when(horarioRepo.findByDiaAndActivoTrue(DiaSemana.LUNES))
                .thenReturn(Optional.empty());

        when(mapper.toEntity(request)).thenReturn(horario);
        when(horarioRepo.save(horario)).thenReturn(horario);
        when(mapper.toDTO(horario)).thenReturn(response);

        HorarioSistemaResponseDTO result = service.crearHorario(request);

        assertNotNull(result);
    }

    @Test
    void testCrearHorario_yaExiste() {
        HorarioSistemaRequestDTO request = new HorarioSistemaRequestDTO();
        request.setDia(DiaSemana.LUNES);

        when(horarioRepo.findByDiaAndActivoTrue(DiaSemana.LUNES))
                .thenReturn(Optional.of(new HorarioSistema()));

        assertThrows(HorarioSistemaException.class, () -> {
            service.crearHorario(request);
        });
    }

    @Test
    void testObtenerHorarioPorDia_ok() {
        HorarioSistema horario = new HorarioSistema();
        HorarioSistemaResponseDTO response = new HorarioSistemaResponseDTO();

        when(horarioRepo.findByDiaAndActivoTrue(DiaSemana.LUNES))
                .thenReturn(Optional.of(horario));

        when(mapper.toDTO(horario)).thenReturn(response);

        HorarioSistemaResponseDTO result = service.obtenerHorarioPorDia(DiaSemana.LUNES);

        assertNotNull(result);
    }

    @Test
    void testObtenerHorarioPorDia_noExiste() {
        when(horarioRepo.findByDiaAndActivoTrue(DiaSemana.LUNES))
                .thenReturn(Optional.empty());

        assertThrows(HorarioSistemaException.class, () -> {
            service.obtenerHorarioPorDia(DiaSemana.LUNES);
        });
    }

    @Test
    void testEliminarHorario_ok() {
        when(horarioRepo.existsById(1L)).thenReturn(true);

        service.eliminarHorario(1L);

        verify(horarioRepo).deleteById(1L);
    }

    @Test
    void testEliminarHorario_noExiste() {
        when(horarioRepo.existsById(1L)).thenReturn(false);

        assertThrows(HorarioSistemaException.class, () -> {
            service.eliminarHorario(1L);
        });
    }

    
    @Test
    void testValidarHorarioActual_sinHorario() {
        when(horarioRepo.findByDiaAndActivoTrue(any()))
            .thenReturn(Optional.empty());
            
        boolean result = service.validarHorarioActual();
        
        assertFalse(result);
    }
}

