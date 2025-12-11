package com.sena.BogotaMetroApp.presentation.dto.soporte;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sena.BogotaMetroApp.presentation.dto.common.IDatosPersonalesUpdate;
import com.sena.BogotaMetroApp.utils.enums.TipoDocumentoEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SoporteUpdateDTO implements IDatosPersonalesUpdate {

    private String nombreCompleto;
    private String correo;
    private String telefono;
    private TipoDocumentoEnum tipoDocumento;
    private String numDocumento;
    private String direccion;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate fechaNacimiento;
}
