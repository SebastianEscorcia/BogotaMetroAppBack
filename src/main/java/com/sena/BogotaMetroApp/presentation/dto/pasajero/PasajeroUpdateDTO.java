package com.sena.BogotaMetroApp.presentation.dto.pasajero;

import java.time.LocalDate;

public record PasajeroUpdateDTO(String nombreCompleto,
                                String direccion,
                                LocalDate fechaNacimiento
) {
}

