package com.sena.BogotaMetroApp.services.pasajeroviaje;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.Viaje;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.persistence.repository.ViajeRepository;
import com.sena.BogotaMetroApp.persistence.repository.qr.QrRepository;
import com.sena.BogotaMetroApp.presentation.dto.pasajeroviaje.PasajeroViajeRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajeroviaje.PasajeroViajeResponseDTO;
import com.sena.BogotaMetroApp.mapper.pasajeroviaje.PasajeroViajeMapper;
import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViaje;
import com.sena.BogotaMetroApp.persistence.repository.pasajeroviaje.PasajeroViajeRepository;
import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasajeroViajeServiceImpl implements IPasajeroViajeService {
    private final PasajeroViajeMapper pasajeroViajeMapper;
    private final PasajeroViajeRepository pasajeroViajeRepo;


    private final UsuarioRepository usuarioRepository;
    private final ViajeRepository viajeRepository;
    private final QrRepository qrRepository;

    @Override
    public PasajeroViajeResponseDTO registrarViaje(PasajeroViajeRequestDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.getIdPasajero())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Viaje viaje = viajeRepository.findById(dto.getIdViaje())
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        Qr qr = new Qr();
        qr.setContenidoQr("VIAJE:" + viaje.getId() + ":" + usuario.getId() + ":" + System.currentTimeMillis());
        qr.setFechaGeneracion(LocalDateTime.now());
        qr.setTipo(TipoQr.VIAJE);
        qr.setUsuario(usuario);
        qrRepository.save(qr);


        PasajeroViaje pv = new PasajeroViaje();
        pv.setPasajero(usuario.getPasajero());
        pv.setViaje(viaje);
        pv.setFechaRegistro(LocalDateTime.now());
        pv.setQr(qr);

        PasajeroViaje guardado = pasajeroViajeRepo.save(pv);

        return pasajeroViajeMapper.toDTO(guardado);
    }

    @Override
    public PasajeroViajeResponseDTO obtenerTicket(Long idTicket) {
        PasajeroViaje pv = pasajeroViajeRepo.findById(idTicket)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        return pasajeroViajeMapper.toDTO(pv);

    }

    @Override
    public List<PasajeroViajeResponseDTO> obtenerTicketsDeUsuarioEnViaje(Long idPasajero, Long idViaje) {
        return pasajeroViajeRepo.findByPasajeroIdAndViajeId(idPasajero, idViaje)
                .stream()
                .map(pasajeroViajeMapper::toDTO)
                .collect(Collectors.toList());
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
