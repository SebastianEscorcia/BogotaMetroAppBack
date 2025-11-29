package com.sena.BogotaMetroApp.services.pasajeroviaje;

import com.sena.BogotaMetroApp.presentation.dto.pasajeroviaje.PasajeroViajeRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajeroviaje.PasajeroViajeResponseDTO;
import com.sena.BogotaMetroApp.mapper.pasajeroviaje.PasajeroViajeMapper;
import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViaje;
import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViajeId;
import com.sena.BogotaMetroApp.persistence.repository.pasajeroviaje.PasajeroViajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasajeroViajeServiceImpl implements IPasajeroViajeService {
    private final PasajeroViajeMapper pasajeroViajeMapper;
    private final PasajeroViajeRepository pasajeroViajeRepo;

    @Override
    public PasajeroViajeResponseDTO registrarViaje(PasajeroViajeRequestDTO dto) {
        return pasajeroViajeMapper.toEntity(dto);
    }

    @Override
    public PasajeroViajeResponseDTO obtenerTicket(Long idPasajero, Long idViaje) {
        PasajeroViajeId id = new PasajeroViajeId(idPasajero, idViaje);


        PasajeroViaje pv = pasajeroViajeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        return pasajeroViajeMapper.toDTO(pv);
    }

    @Override
    public List<PasajeroViajeResponseDTO> obtenerViajesPorPasajero(Long idPasajero) {
        return pasajeroViajeRepo.findByPasajeroId(idPasajero)
                .stream()
                .map(pasajeroViajeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PasajeroViajeResponseDTO> obtenerPasajerosDeViaje(Long idViaje) {
        return pasajeroViajeRepo.findByViajeId(idViaje)
                .stream()
                .map(pasajeroViajeMapper::toDTO)
                .collect(Collectors.toList());
    }
}
