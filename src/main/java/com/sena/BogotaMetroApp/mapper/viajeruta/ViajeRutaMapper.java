package com.sena.BogotaMetroApp.mapper.viajeruta;

import com.sena.BogotaMetroApp.presentation.dto.viajeruta.ViajeRutaResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.viajeruta.ViajeRuta;
import org.springframework.stereotype.Component;

@Component
public class ViajeRutaMapper {

    public ViajeRutaResponseDTO toDTO(ViajeRuta vr) {
        ViajeRutaResponseDTO dto = new ViajeRutaResponseDTO();
        dto.setIdViaje(vr.getViaje().getId());
        dto.setIdRuta(vr.getRuta().getId());
        return dto;
    }
}
