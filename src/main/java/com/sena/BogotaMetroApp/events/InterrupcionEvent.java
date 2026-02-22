package com.sena.BogotaMetroApp.events;

import com.sena.BogotaMetroApp.utils.enums.AccionNotificationEnum;

public record InterrupcionEvent(AccionNotificationEnum accion , Object payload) {
}
