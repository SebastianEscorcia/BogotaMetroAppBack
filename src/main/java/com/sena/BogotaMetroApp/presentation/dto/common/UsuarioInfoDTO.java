package com.sena.BogotaMetroApp.presentation.dto.common;

import com.sena.BogotaMetroApp.utils.enums.TipoDocumentoEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UsuarioInfoDTO {
    private Long idUsuario;
    private String correo;

    private String nombreCompleto;
    private String telefono;
    private TipoDocumentoEnum tipoDocumento;
    private String numDocumento;
    private LocalDate fechaNacimiento;
    private String direccion;
}
