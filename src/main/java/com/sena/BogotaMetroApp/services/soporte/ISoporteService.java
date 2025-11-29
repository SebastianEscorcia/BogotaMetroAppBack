package com.sena.BogotaMetroApp.services.soporte;

import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteResponseDTO;

public interface ISoporteService {
    SoporteResponseDTO registrar (SoporteRequestDTO dto);
}
