package com.sena.BogotaMetroApp.presentation.dto.sesionchat;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SesionChatResponseDTO {
    private Long id;
    private String estado;
    private LocalDateTime fechaUltimaActividad;
    private LocalDateTime fechaAsignacion;
    private List<ParticipanteDTO> participantes;

}
