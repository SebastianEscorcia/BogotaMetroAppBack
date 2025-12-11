package com.sena.BogotaMetroApp.presentation.dto.common;

import com.sena.BogotaMetroApp.utils.enums.TipoDocumentoEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioInfoDTO {
    private Long idUsuario;
    private String correo;

    private String nombreCompleto;
    private String telefono;
    private TipoDocumentoEnum tipoDocumento;
    private String numDocumento;
}
