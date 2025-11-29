package com.sena.BogotaMetroApp.services.estacion;

import com.sena.BogotaMetroApp.presentation.dto.estacion.EstacionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.estacion.EstacionResponseDTO;
import com.sena.BogotaMetroApp.mapper.EstacionMapper;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EstacionServicesImpl implements IEstacionServices {

    private final EstacionRepository estacionRepository;
    private final EstacionMapper estacionMapper;

    @Override
    public EstacionResponseDTO crear(EstacionRequestDTO dto) {
        Estacion estacion = new Estacion();
        estacion.setNombre(dto.getNombre());
        estacion.setLatitud(dto.getLatitud());
        estacion.setLongitud(dto.getLongitud());
        estacion.setEsAccesible(dto.getEsAccesible());
        estacion.setTipo(dto.getTipo());

        Estacion e = estacionRepository.save(estacion);

        return estacionMapper.toDTO(e);
    }

    @Override
    public EstacionResponseDTO obtener(Long id) {
        Estacion estacion = estacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estación no encontrada"));
        return estacionMapper.toDTO(estacion);
    }

    @Override
    public List<EstacionResponseDTO> listar() {
        return estacionRepository.findAll().stream()
                .map(estacionMapper::toDTO)
                .toList();
    }

    @Override
    public EstacionResponseDTO actualizar(Long id, EstacionRequestDTO dto) {
        Estacion estacion = estacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estación no encontrada"));

        estacion.setNombre(dto.getNombre());
        estacion.setLatitud(dto.getLatitud());
        estacion.setLongitud(dto.getLongitud());
        estacion.setEsAccesible(dto.getEsAccesible());
        estacion.setTipo(dto.getTipo());

        return estacionMapper.toDTO(estacionRepository.save(estacion));
    }

    @Override
    public void eliminar(Long id) {
        estacionRepository.deleteById(id);
    }
}
