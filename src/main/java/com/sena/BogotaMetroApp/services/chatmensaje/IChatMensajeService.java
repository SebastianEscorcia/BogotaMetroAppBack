package com.sena.BogotaMetroApp.services.chatmensaje;

import com.sena.BogotaMetroApp.presentation.dto.MensajeDTO;

public interface IChatMensajeService {
    MensajeDTO procesarYGuardarMensaje(Long idSesion,  MensajeDTO mensajeDTO);
}
