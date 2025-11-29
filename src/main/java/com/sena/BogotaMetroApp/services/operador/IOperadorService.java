package com.sena.BogotaMetroApp.services.operador;

import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorResponseDTO;

public interface IOperadorService {
    OperadorResponseDTO registrar(OperadorRequestDTO dto);

}
