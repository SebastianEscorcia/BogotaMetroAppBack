package com.sena.BogotaMetroApp.presentation.dto.pasajero;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PasajeroUpdateDTO {

    private String nombreCompleto;
    private String telefono;
    private String tipoDocumento;
    private String numDocumento;
    private Date fechaNacimiento;
    private String direccion;
}