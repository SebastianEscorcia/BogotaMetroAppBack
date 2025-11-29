package com.sena.BogotaMetroApp.mapper.pasajeroviaje;

import com.sena.BogotaMetroApp.presentation.dto.pasajeroviaje.PasajeroViajeRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajeroviaje.PasajeroViajeResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.Viaje;
import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViaje;
import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViajeId;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.persistence.repository.ViajeRepository;
import com.sena.BogotaMetroApp.persistence.repository.pasajeroviaje.PasajeroViajeRepository;
import com.sena.BogotaMetroApp.persistence.repository.qr.QrRepository;
import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PasajeroViajeMapper {
    private final PasajeroViajeRepository pasajeroViajeRepo;
    private final UsuarioRepository usuarioRepository;
    private final ViajeRepository viajeRepository;
    private final QrRepository qrRepository;

    public PasajeroViajeResponseDTO toEntity(PasajeroViajeRequestDTO dto) {

        boolean existe = pasajeroViajeRepo
                .existsByIdPasajeroIdAndIdViajeId(dto.getIdPasajero(), dto.getIdViaje());

        if (existe) {
            throw new RuntimeException("El pasajero ya está registrado en este viaje.");
        }

        Usuario usuario = usuarioRepository.findById(dto.getIdPasajero())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Viaje viaje = viajeRepository.findById(dto.getIdViaje())
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));


        PasajeroViajeId id = new PasajeroViajeId(dto.getIdPasajero(), dto.getIdViaje());


        PasajeroViaje pv = new PasajeroViaje();
        pv.setId(id);
        pv.setPasajero(usuario.getPasajero());
        pv.setViaje(viaje);
        pv.setFechaRegistro(LocalDateTime.now());


        Qr qr = new Qr();
        qr.setContenidoQr("VIAJE:" + viaje.getId() + ":" + usuario.getId()); // Ejemplo
        qr.setFechaGeneracion(LocalDateTime.now());
        qr.setTipo(TipoQr.VIAJE);
        qr.setUsuario(usuario);

        qrRepository.save(qr);

        pv.setQr(qr);

        pasajeroViajeRepo.save(pv);

        return toDTO(pv);
    }

    public PasajeroViajeResponseDTO toDTO(PasajeroViaje pasajeroViaje) {
        PasajeroViajeResponseDTO dto = new PasajeroViajeResponseDTO();
        dto.setIdViaje(pasajeroViaje.getPasajero().getId());
        dto.setIdPasajero(pasajeroViaje.getPasajero().getId());
        dto.setIdViaje(pasajeroViaje.getViaje().getId());
        dto.setFechaRegistro(pasajeroViaje.getFechaRegistro().toString());
        dto.setIdQr(pasajeroViaje.getQr() != null ? pasajeroViaje.getQr().getId() : null);
        return dto;
    }
}
