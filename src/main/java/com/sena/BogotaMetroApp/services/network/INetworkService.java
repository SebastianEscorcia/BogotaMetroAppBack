package com.sena.BogotaMetroApp.services.network;

import com.sena.BogotaMetroApp.presentation.dto.mapa.MapaLineaDTO;

import java.util.List;

public interface INetworkService {
    public List<MapaLineaDTO> obtenerMapaCompleto();
}
