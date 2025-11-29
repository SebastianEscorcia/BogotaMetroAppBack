package com.sena.BogotaMetroApp.services.horariolinea;

import com.sena.BogotaMetroApp.presentation.dto.horariolinea.HorarioLineaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.horariolinea.HorarioLineaResponseDTO;
import com.sena.BogotaMetroApp.mapper.HorarioLineaMapper;
import com.sena.BogotaMetroApp.persistence.models.Linea;
import com.sena.BogotaMetroApp.persistence.models.horariolinea.HorarioLinea;
import com.sena.BogotaMetroApp.persistence.repository.horariolinea.HorarioLineaRepository;
import com.sena.BogotaMetroApp.persistence.repository.linea.LineaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioLineaServicesImpl implements IHorarioLineaServices {
    private final HorarioLineaRepository repository;
    private final LineaRepository lineaRepository;
    private final HorarioLineaMapper mapper;

    @Override
    public HorarioLineaResponseDTO crear(HorarioLineaRequestDTO dto) {
        Linea linea = lineaRepository.findById(dto.getIdLinea())
                .orElseThrow(() -> new RuntimeException("Línea no encontrada"));

        HorarioLinea h = new HorarioLinea();
        h.setLinea(linea);
        h.setDia(dto.getDia());
        h.setHoraInicio(dto.getHoraInicio());
        h.setHoraFin(dto.getHoraFin());

        return mapper.toDTO(repository.save(h));
    }

    @Override
    public List<HorarioLineaResponseDTO> listar() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
