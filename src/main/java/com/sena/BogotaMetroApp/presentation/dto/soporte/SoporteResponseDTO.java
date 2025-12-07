package com.sena.BogotaMetroApp.presentation.dto.soporte;

import com.sena.BogotaMetroApp.presentation.dto.common.UsuarioInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SoporteResponseDTO {

    private Long id;
    private String correo;
    private Integer estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimoAcceso;
    private UsuarioInfoDTO usuario;
}
