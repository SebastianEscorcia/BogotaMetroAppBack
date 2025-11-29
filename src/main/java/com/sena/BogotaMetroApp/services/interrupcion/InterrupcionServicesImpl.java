package com.sena.BogotaMetroApp.services.interrupcion;

import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionResponseDTO;
import com.sena.BogotaMetroApp.mapper.InterrupcionMapper;
import com.sena.BogotaMetroApp.persistence.models.Linea;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import com.sena.BogotaMetroApp.persistence.models.interrupcion.Interrupcion;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionRepository;
import com.sena.BogotaMetroApp.persistence.repository.interrupcion.InterrupcionRepository;
import com.sena.BogotaMetroApp.persistence.repository.linea.LineaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class InterrupcionServicesImpl implements IInterrupcionServices {

    private final InterrupcionRepository repository;
    private final EstacionRepository estacionRepository;
    private final LineaRepository lineaRepository;
    private final InterrupcionMapper mapper;

    @Override
    public InterrupcionResponseDTO crear(InterrupcionRequestDTO dto) {
        Estacion estacion = estacionRepository.findById(dto.getIdEstacion())
                .orElseThrow(() -> new RuntimeException("Estación no encontrada"));

        Linea linea = lineaRepository.findById(dto.getIdLinea())
                .orElseThrow(() -> new RuntimeException("Línea no encontrada"));

        Interrupcion inter = new Interrupcion();
        inter.setEstacion(estacion);
        inter.setLinea(linea);
        inter.setTipo(dto.getTipo());
        inter.setDescripcion(dto.getDescripcion());
        inter.setInicio(dto.getInicio());
        inter.setFin(dto.getFin());
        return mapper.toDTO(repository.save(inter));
    }

    @Override
    public List<InterrupcionResponseDTO> listar() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
