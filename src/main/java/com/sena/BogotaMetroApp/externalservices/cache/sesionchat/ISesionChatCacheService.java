package com.sena.BogotaMetroApp.externalservices.cache.sesionchat;

import com.sena.BogotaMetroApp.presentation.dto.sesionchat.SesionChatResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ISesionChatCacheService {
    void cachearSesionesPendientes(List<SesionChatResponseDTO> sesionesPendientes);
    Optional<List<SesionChatResponseDTO>> obtenerSesionesPendientesCacheadas();
    void invalidarSesionesPendientesCache();
    void cachearSesionesActivas(Long idUsuario,  List<SesionChatResponseDTO> sesionesActivas);
    Optional<List<SesionChatResponseDTO>> obtenerSesionesActivasCacheadas(Long idUsuario);
    void invalidarSesionesActivasCache(Long idUsuario);
    void invalidarTodasLasSesionesActivasCache();
}
