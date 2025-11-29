package com.sena.BogotaMetroApp.services.ruta;

import com.sena.BogotaMetroApp.presentation.dto.ruta.RutaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.ruta.RutaResponseDTO;
import com.sena.BogotaMetroApp.mapper.RutaMapper;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import com.sena.BogotaMetroApp.persistence.models.Ruta;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionRepository;
import com.sena.BogotaMetroApp.persistence.repository.RutaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RutaServicesImpl implements IRutaServices {
    private final RutaRepository rutaRepository;
    private final EstacionRepository estacionRepository;
    private final RutaMapper rutaMapper;

    @Override
    public RutaResponseDTO crear(RutaRequestDTO dto) {
        Ruta ruta = new Ruta();

        ruta.setDistancia(dto.getDistancia());
        ruta.setHoraInicioRuta(dto.getHoraInicioRuta());
        ruta.setFecha(dto.getFecha());

        Estacion inicio = estacionRepository.findById(dto.getEstacionInicioId())
                .orElseThrow(() -> new RuntimeException("Estación inicio no encontrada"));

        Estacion fin = estacionRepository.findById(dto.getEstacionFinId())
                .orElseThrow(() -> new RuntimeException("Estación fin no encontrada"));

        ruta.setEstacionInicio(inicio);
        ruta.setEstacionFin(fin);

        Ruta saved = rutaRepository.save(ruta);

        return rutaMapper.toDTO(saved);
    }

    @Override
    public RutaResponseDTO obtener(Long id) {
        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));
        return rutaMapper.toDTO(ruta);
    }

    @Override
    public List<RutaResponseDTO> listar() {
        return rutaRepository.findAll().stream()
                .map(rutaMapper::toDTO)
                .toList();
    }

    @Override
    public RutaResponseDTO actualizar(Long id, RutaRequestDTO dto) {
        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));

        ruta.setDistancia(dto.getDistancia());
        ruta.setHoraInicioRuta(dto.getHoraInicioRuta());
        ruta.setFecha(dto.getFecha());

        Estacion inicio = estacionRepository.findById(dto.getEstacionInicioId())
                .orElseThrow(() -> new RuntimeException("Estación inicio no encontrada"));

        Estacion fin = estacionRepository.findById(dto.getEstacionFinId())
                .orElseThrow(() -> new RuntimeException("Estación fin no encontrada"));

        ruta.setEstacionInicio(inicio);
        ruta.setEstacionFin(fin);

        return rutaMapper.toDTO(rutaRepository.save(ruta));
    }

    @Override
    public void eliminar(Long id) {
        rutaRepository.deleteById(id);
    }
}
