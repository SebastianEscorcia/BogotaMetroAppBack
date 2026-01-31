package com.sena.BogotaMetroApp.presentation.dto.sesionchat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParticipanteDTO {
    private Long id;
    private Long idUsuario;
    private String nombreUsuario;
    private String correo;
    private LocalDateTime fechaUnion;
    private boolean activo;
}
