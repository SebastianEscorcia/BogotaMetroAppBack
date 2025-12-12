package com.sena.BogotaMetroApp.services.puntointeres;

import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionRepository;
import com.sena.BogotaMetroApp.presentation.dto.puntointeres.PuntoInteresRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.puntointeres.PuntoInteresResponseDTO;
import com.sena.BogotaMetroApp.mapper.puntointeres.PuntoInteresMapper;
import com.sena.BogotaMetroApp.persistence.models.puntointeres.PuntoInteres;
import com.sena.BogotaMetroApp.persistence.repository.puntointeres.PuntoInteresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PuntoInteresServicesImpl implements IPuntoInteresService {
    private final PuntoInteresRepository poiRepository;
    private final PuntoInteresMapper mapper;
    private final EstacionRepository estacionRepository;

    @Override
    public PuntoInteresResponseDTO crear(PuntoInteresRequestDTO dto) {
        PuntoInteres p = mapper.toEntity(dto);
        return mapper.toDTO(poiRepository.save(p));
    }

    @Override
    public List<PuntoInteresResponseDTO> listar() {
        return poiRepository.findByActivoTrue()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public PuntoInteresResponseDTO actualizar(Long id, PuntoInteresRequestDTO dto) {
        PuntoInteres poi = poiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Punto de interés no encontrado"));

        if (!poi.isActivo()) {
            throw new RuntimeException("El punto de interés fue eliminado");
        }

        if (dto.getIdEstacion() != null && !dto.getIdEstacion().equals(poi.getEstacion().getId())) {

            Estacion nuevaEstacion = estacionRepository.findById(dto.getIdEstacion())
                    .orElseThrow(() -> new RuntimeException("La nueva estación especificada no existe"));

            if (!nuevaEstacion.isActivo()) {
                throw new RuntimeException("No se puede asignar una estación eliminada");
            }

            poi.setEstacion(nuevaEstacion);
        }
        poi.setNombre(dto.getNombre());
        poi.setCategoria(dto.getCategoria());
        poi.setLatitud(dto.getLatitud());
        poi.setLongitud(dto.getLongitud());
        return mapper.toDTO(poiRepository.save(poi));
    }

    @Override
    public void eliminar(Long id) {
        PuntoInteres poi = poiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Punto de interés no encontrado"));
        poi.setActivo(false);
        poiRepository.save(poi);
    }
}
