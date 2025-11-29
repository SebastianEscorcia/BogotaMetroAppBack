package com.sena.BogotaMetroApp.presentation.dto.pasajero;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RegistroPasajeroUnificadoDTO {
    @NotNull
    private String correo;

    @NotNull
    private String clave;

    private String nombreCompleto;
    private String telefono;
    private String tipoDocumento;
    private String numDocumento;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date fechaNacimiento;
    private String direccion;
}
