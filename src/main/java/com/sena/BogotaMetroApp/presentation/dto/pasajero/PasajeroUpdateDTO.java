package com.sena.BogotaMetroApp.presentation.dto.pasajero;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PasajeroUpdateDTO {

    private String nombreCompleto;
    private String telefono;
    private String tipoDocumento;
    private String numDocumento;
    private LocalDate fechaNacimiento;
    private String direccion;
}