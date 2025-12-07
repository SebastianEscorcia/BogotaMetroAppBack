package com.sena.BogotaMetroApp.presentation.dto.interrupcion;

import com.sena.BogotaMetroApp.utils.enums.AccionNotificationEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionInterrupcionDTO {
    private AccionNotificationEnum accion;
    private Object payload;
}
