package com.sena.BogotaMetroApp.services.horariosistema;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.horariosistema.HorarioSistema;
import com.sena.BogotaMetroApp.persistence.repository.horariosistema.HorarioSistemaRepository;
import com.sena.BogotaMetroApp.presentation.dto.horariosistema.HorarioSistemaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.horariosistema.HorarioSistemaResponseDTO;
import com.sena.BogotaMetroApp.services.exception.horariosistema.HorarioSistemaException;
import com.sena.BogotaMetroApp.mapper.horariosistema.HorarioSistemaMapper;
import com.sena.BogotaMetroApp.utils.enums.DiaSemana;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioSistemaServiceImpl implements IHorarioSistemaService {

    private final HorarioSistemaRepository horarioRepo;
    private final HorarioSistemaMapper mapper;

    @Override
    public HorarioSistemaResponseDTO crearHorario(HorarioSistemaRequestDTO request) {
        boolean existeHorario = horarioRepo.findByDiaAndActivoTrue(request.getDia()).isPresent();

        if (existeHorario) {
            throw new HorarioSistemaException(ErrorCodeEnum.HORARIO_ALREADY_EXISTS);
        }

        HorarioSistema horario = mapper.toEntity(request);
        HorarioSistema guardado = horarioRepo.save(horario);
        return mapper.toDTO(guardado);
    }

    @Override
    public HorarioSistemaResponseDTO obtenerHorarioPorDia(DiaSemana dia) {
        HorarioSistema horario = horarioRepo.findByDiaAndActivoTrue(dia)
                .orElseThrow(() -> new HorarioSistemaException(ErrorCodeEnum.HORARIO_NOT_FOUND));
        return mapper.toDTO(horario);
    }

    @Override
    public List<HorarioSistemaResponseDTO> obtenerTodosHorarios() {
        return horarioRepo.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HorarioSistemaResponseDTO actualizarHorario(Long id, HorarioSistemaRequestDTO request) {
        HorarioSistema horario = horarioRepo.findById(id)
                .orElseThrow(() -> new HorarioSistemaException(ErrorCodeEnum.HORARIO_NOT_FOUND));

        boolean existeOtroHorarioConMismoDia = horarioRepo
                .findByDiaAndActivoTrueAndIdNot(request.getDia(), id)
                .isPresent();

        if (existeOtroHorarioConMismoDia) {
            throw new HorarioSistemaException(ErrorCodeEnum.HORARIO_ALREADY_EXISTS);
        }

        mapper.updateEntity(horario, request);
        HorarioSistema actualizado = horarioRepo.save(horario);
        return mapper.toDTO(actualizado);
    }

    @Override
    public void eliminarHorario(Long id) {
        if (!horarioRepo.existsById(id)) {
            throw new HorarioSistemaException(ErrorCodeEnum.HORARIO_NOT_FOUND);
        }
        horarioRepo.deleteById(id);
    }

    @Override
    public boolean validarHorarioActual() {
        DiaSemana diaActual = DiaSemana.fromLocalDate(LocalDate.now());
        LocalTime horaActual = LocalTime.now();

        HorarioSistema horario = horarioRepo.findByDiaAndActivoTrue(diaActual).orElse(null);
        if (horario == null) {
            return false;
        }

        return !horaActual.isBefore(horario.getHoraInicio()) && !horaActual.isAfter(horario.getHoraFin());
    }
}