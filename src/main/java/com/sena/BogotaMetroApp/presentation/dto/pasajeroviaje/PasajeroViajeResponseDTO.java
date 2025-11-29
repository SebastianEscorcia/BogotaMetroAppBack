package com.sena.BogotaMetroApp.presentation.dto.pasajeroviaje;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasajeroViajeResponseDTO {
    private Long idPasajero;
    private Long idViaje;
    private String fechaRegistro;
    private Long idQr;
}
