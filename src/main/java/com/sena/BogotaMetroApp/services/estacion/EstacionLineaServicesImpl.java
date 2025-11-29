package com.sena.BogotaMetroApp.services.estacion;

import com.sena.BogotaMetroApp.presentation.dto.estacionlinea.EstacionLineaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.estacionlinea.EstacionLineaResponseDTO;
import com.sena.BogotaMetroApp.mapper.EstacionLineaMapper;
import com.sena.BogotaMetroApp.persistence.models.Linea;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import com.sena.BogotaMetroApp.persistence.models.estacion.EstacionLinea;
import com.sena.BogotaMetroApp.persistence.models.estacion.EstacionLineaId;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionLineaRepository;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionRepository;
import com.sena.BogotaMetroApp.persistence.repository.linea.LineaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstacionLineaServicesImpl implements IEstacionLineaServices {

    private final EstacionLineaRepository repository;
    private final LineaRepository lineaRepository;
    private final EstacionRepository estacionRepository;
    private  final EstacionLineaMapper mapper;
    @Override
    public EstacionLineaResponseDTO crear(EstacionLineaRequestDTO dto) {
        Linea linea = lineaRepository.findById(dto.getIdLinea())
                .orElseThrow(() -> new RuntimeException("Línea no encontrada"));

        Estacion estacion = estacionRepository.findById(dto.getIdEstacion())
                .orElseThrow(() -> new RuntimeException("Estación no encontrada"));

        EstacionLineaId id = new EstacionLineaId(dto.getIdLinea(), dto.getIdEstacion());

        EstacionLinea relacion = new EstacionLinea();
        relacion.setId(id);
        relacion.setLinea(linea);
        relacion.setEstacion(estacion);
        relacion.setOrden(dto.getOrden());

        EstacionLinea guardada = repository.save(relacion);
        return mapper.toDTO(guardada);

    }

    @Override
    public List<EstacionLineaResponseDTO> listar() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public void eliminar(Long idLinea, Long idEstacion) {
        EstacionLineaId id = new EstacionLineaId(idLinea, idEstacion);
        repository.deleteById(id);
    }
}
