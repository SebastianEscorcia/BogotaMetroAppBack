package com.sena.BogotaMetroApp.presentation.dto.operador;

import com.sena.BogotaMetroApp.utils.enums.TipoDocumentoEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperadorResponseDTO {

    private Long id;
    private Long idUsuario;
    private String correo;
    private String nombreCompleto;
    private String telefono;
    private TipoDocumentoEnum tipoDocumento;
    private String numDocumento;
    private boolean activo;
}
