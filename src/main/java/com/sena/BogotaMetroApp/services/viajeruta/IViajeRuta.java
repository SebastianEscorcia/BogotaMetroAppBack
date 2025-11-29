package com.sena.BogotaMetroApp.services.viajeruta;

import com.sena.BogotaMetroApp.presentation.dto.viajeruta.ViajeRutaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.viajeruta.ViajeRutaResponseDTO;

public interface IViajeRuta {
    ViajeRutaResponseDTO asignarRuta(ViajeRutaRequestDTO dto);
}

