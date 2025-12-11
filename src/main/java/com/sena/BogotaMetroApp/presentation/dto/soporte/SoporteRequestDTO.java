package com.sena.BogotaMetroApp.presentation.dto.soporte;

import com.sena.BogotaMetroApp.presentation.dto.usuario.RegistroUsuarioBaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SoporteRequestDTO extends RegistroUsuarioBaseDTO {

    private Integer estado;
}
