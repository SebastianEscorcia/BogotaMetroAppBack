package com.sena.BogotaMetroApp.presentation.dto.soporte;

import com.sena.BogotaMetroApp.presentation.dto.usuario.RegistroUsuarioBaseDTO;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class SoporteRequestDTO extends RegistroUsuarioBaseDTO {

    @NotNull
    private Integer estado;
}
