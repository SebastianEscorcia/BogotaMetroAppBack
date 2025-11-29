package com.sena.BogotaMetroApp.services.viajeruta;

import com.sena.BogotaMetroApp.presentation.dto.viajeruta.ViajeRutaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.viajeruta.ViajeRutaResponseDTO;
import com.sena.BogotaMetroApp.mapper.viajeruta.ViajeRutaMapper;
import com.sena.BogotaMetroApp.persistence.models.Ruta;
import com.sena.BogotaMetroApp.persistence.models.Viaje;
import com.sena.BogotaMetroApp.persistence.models.viajeruta.ViajeRuta;
import com.sena.BogotaMetroApp.persistence.repository.RutaRepository;
import com.sena.BogotaMetroApp.persistence.repository.ViajeRepository;
import com.sena.BogotaMetroApp.persistence.repository.viajeruta.ViajeRutaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViajeRutaImpl implements IViajeRuta {
    private final ViajeRutaRepository viajeRutaRepository;
    private final ViajeRepository viajeRepository;
    private final RutaRepository rutaRepository;
    private final ViajeRutaMapper mapper;

    @Override
    public ViajeRutaResponseDTO asignarRuta(ViajeRutaRequestDTO dto) {
        Viaje viaje = viajeRepository.findById(dto.getIdViaje())
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        Ruta ruta = rutaRepository.findById(dto.getIdRuta())
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));

        ViajeRuta vr = new ViajeRuta();
        vr.setViaje(viaje);
        vr.setRuta(ruta);

        return mapper.toDTO(viajeRutaRepository.save(vr));
    }
}
