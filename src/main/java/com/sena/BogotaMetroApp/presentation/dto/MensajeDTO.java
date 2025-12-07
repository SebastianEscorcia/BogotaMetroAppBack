package com.sena.BogotaMetroApp.presentation.dto;

import com.sena.BogotaMetroApp.utils.enums.TipoRemitenteEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MensajeDTO {
    private String contenido;
    private TipoRemitenteEnum tipoRemitente;
    private  Long remitenteId;
    private LocalDateTime fechaEnvio;
}
