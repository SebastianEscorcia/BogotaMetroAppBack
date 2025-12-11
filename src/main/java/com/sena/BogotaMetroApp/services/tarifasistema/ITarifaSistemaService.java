package com.sena.BogotaMetroApp.services.tarifasistema;

import com.sena.BogotaMetroApp.presentation.dto.tarifasistema.TarifaSistemaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.tarifasistema.TarifaSistemaResponseDTO;

import java.math.BigDecimal;

public interface ITarifaSistemaService {

    TarifaSistemaResponseDTO crearTarifa(TarifaSistemaRequestDTO request);
    TarifaSistemaResponseDTO obtenerTarifaActiva();
    TarifaSistemaResponseDTO actualizarTarifa(Long id, TarifaSistemaRequestDTO request);
    void eliminarTarifa(Long id);
    BigDecimal obtenerValorTarifaActual();
}
