package com.sena.BogotaMetroApp.presentation.dto.pasajero;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PasajeroRequestDTO {
    @NotNull
    private Long idUsuario;
    private String correo;
    private String clave;
    private String nombreCompleto;
    private String telefono;
    private String tipoDocumento;
    private String numDocumento;
    private Date fechaNacimiento;
    private String direccion;
}
