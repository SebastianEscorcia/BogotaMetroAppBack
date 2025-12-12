package com.sena.BogotaMetroApp.utils.enums;

import com.sena.BogotaMetroApp.utils.holders.HolidayBridge;

import java.time.LocalDate;

public enum DiaSemana {
    LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO, FESTIVO;



    public static DiaSemana fromLocalDate(LocalDate date) {
        if (HolidayBridge.isHoliday(date)) {
            return FESTIVO;
        }

        return switch (date.getDayOfWeek()) {
            case MONDAY -> LUNES;
            case TUESDAY -> MARTES;
            case WEDNESDAY -> MIERCOLES;
            case THURSDAY -> JUEVES;
            case FRIDAY -> VIERNES;
            case SATURDAY -> SABADO;
            case SUNDAY -> DOMINGO;
            default -> throw new IllegalArgumentException("Día inválido: " + date.getDayOfWeek());
        };
    }
}
