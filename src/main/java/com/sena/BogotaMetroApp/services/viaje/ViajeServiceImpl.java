package com.sena.BogotaMetroApp.services.viaje;

import com.sena.BogotaMetroApp.presentation.dto.viaje.ViajeRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.viaje.ViajeResponseDTO;
import com.sena.BogotaMetroApp.mapper.ViajeMapper;
import com.sena.BogotaMetroApp.persistence.models.Ruta;
import com.sena.BogotaMetroApp.persistence.models.Viaje;
import com.sena.BogotaMetroApp.persistence.repository.RutaRepository;
import com.sena.BogotaMetroApp.persistence.repository.ViajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ViajeServiceImpl implements IViajeServices {

    private final ViajeRepository viajeRepository;
    private final RutaRepository rutaRepository;
    private final ViajeMapper mapper;

    @Override
    public ViajeResponseDTO crearViaje(ViajeRequestDTO dto) {

        Viaje viaje = new Viaje();

        viaje.setNombreViaje(dto.getNombreViaje());
        viaje.setFechaHoraInicio(dto.getFechaHoraInicio());
        viaje.setFechaHoraFin(dto.getFechaHoraFin());
        viaje.setDescripcion(dto.getDescripcion());
        viaje.setPresupuesto(dto.getPresupuesto());
        viaje.setEstadoViaje(dto.getEstadoViaje());

        // Cargar rutas asociadas
        List<Ruta> rutas = rutaRepository.findAllById(dto.getRutasIds());
        viaje.setRutas(rutas);

        Viaje guardado = viajeRepository.save(viaje);

        return mapper.toDTO(guardado);
    }

    @Override
    public List<ViajeResponseDTO> listarViajes() {
        return viajeRepository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public ViajeResponseDTO obtenerViaje(Long id) {
        Viaje viaje = viajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        return mapper.toDTO(viaje);
    }
}
