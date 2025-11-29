package com.sena.BogotaMetroApp.services.usuario;

import com.sena.BogotaMetroApp.presentation.dto.usuario.UsuarioRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.usuario.UsuarioResponseDTO;

import java.util.List;

public interface IUsuarioServices {
    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto);

    List<UsuarioResponseDTO> listarUsuarios();

    UsuarioResponseDTO obtenerPorId(Long id);


}

