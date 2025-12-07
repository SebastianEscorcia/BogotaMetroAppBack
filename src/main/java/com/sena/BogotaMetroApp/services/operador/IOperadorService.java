package com.sena.BogotaMetroApp.services.operador;

import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.operador.RegistroOperadorDTO;

public interface IOperadorService {
    OperadorResponseDTO registrar(RegistroOperadorDTO dto);

}
