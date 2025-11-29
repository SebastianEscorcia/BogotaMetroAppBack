package com.sena.BogotaMetroApp.services.conexion;

import com.sena.BogotaMetroApp.presentation.dto.conexion.ConexionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.conexion.ConexionResponseDTO;

import java.util.List;

public interface IConexionService {
    ConexionResponseDTO crear(ConexionRequestDTO dto);
    List<ConexionResponseDTO> listar();
}
