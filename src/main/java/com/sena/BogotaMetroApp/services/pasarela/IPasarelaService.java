package com.sena.BogotaMetroApp.services.pasarela;

import com.sena.BogotaMetroApp.presentation.dto.pasarela.PasarelaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasarela.PasarelaResponseDTO;

import java.util.List;

public interface IPasarelaService {

    PasarelaResponseDTO crearPasarela(PasarelaRequestDTO dto);

    PasarelaResponseDTO obtenerPasarelaPorId(Long id);

    List<PasarelaResponseDTO> obtenerTodasLasPasarelas();

    PasarelaResponseDTO obtenerPasarelaPorNombre(String nombre);

    PasarelaResponseDTO obtenerPasarelaPorCodigo(Integer codigo);

    List<PasarelaResponseDTO> obtenerPasarelasPorPais(String pais);

    PasarelaResponseDTO actualizarPasarela(Long id, PasarelaRequestDTO dto);

    void eliminarPasarela(Long id);
}
