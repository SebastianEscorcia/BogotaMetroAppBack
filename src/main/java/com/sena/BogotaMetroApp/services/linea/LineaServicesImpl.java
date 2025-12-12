package com.sena.BogotaMetroApp.services.linea;

import com.sena.BogotaMetroApp.presentation.dto.linea.LineaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.linea.LineaResponseDTO;
import com.sena.BogotaMetroApp.mapper.LineaMapper;
import com.sena.BogotaMetroApp.persistence.models.Linea;
import com.sena.BogotaMetroApp.persistence.repository.linea.LineaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LineaServicesImpl implements ILineaServices {
    private final LineaRepository lineaRepository;
    private final LineaMapper lineaMapper;

    @Override
    public LineaResponseDTO crear(LineaRequestDTO dto) {
        Linea linea = new Linea();
        linea.setNombre(dto.getNombre());
        linea.setColor(dto.getColor());
        linea.setFrecuenciaMinutos(dto.getFrecuenciaMinutos());

        Linea guardada = lineaRepository.save(linea);
        return lineaMapper.toDTO(guardada);
    }

    @Override
    public LineaResponseDTO obtener(Long id) {
        Linea linea = lineaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Línea no encontrada"));
        if (!linea.isActivo()) throw new RuntimeException("Línea no encontrada (Eliminada)");

        return lineaMapper.toDTO(linea);
    }

    @Override
    public List<LineaResponseDTO> listar() {
        return lineaRepository.findByActivoTrue().stream()
                .map(lineaMapper::toDTO)
                .toList();
    }

    @Override
    public LineaResponseDTO actualizar(Long id, LineaRequestDTO dto) {
        Linea linea = lineaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Línea no encontrada"));

        linea.setNombre(dto.getNombre());
        linea.setColor(dto.getColor());
        linea.setFrecuenciaMinutos(dto.getFrecuenciaMinutos());

        Linea actualizada = lineaRepository.save(linea);
        return lineaMapper.toDTO(actualizada);

    }

    @Override
    public void eliminar(Long id) {
        Linea linea = lineaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Línea no encontrada"));
        linea.setActivo(false);
        lineaRepository.save(linea);
    }
}
