package com.sena.BogotaMetroApp.presentation.dto.pasajero;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasajeroResponseDTO {

    private Long id;
    private Long idUsuario;
    private String correo;
    private String nombreCompleto;
    private String telefono;
    private String tipoDocumento;
    private String numDocumento;
}
