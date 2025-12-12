package com.sena.BogotaMetroApp.utils.logic;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class HolidayService {

    /* -----------------------------------------------------------
     *   1. FESTIVOS PRO — COLOMBIA
     * ----------------------------------------------------------- */

    @Cacheable(value = "colombianHolidays", key = "#year")
    public Set<LocalDate> getColombianHolidays(int year) {

        Set<LocalDate> holidays = new HashSet<>();

        // ------------------ FESTIVOS FIJOS ------------------
        holidays.add(LocalDate.of(year, 1, 1));    // Año Nuevo
        holidays.add(LocalDate.of(year, 5, 1));    // Día del Trabajo
        holidays.add(LocalDate.of(year, 7, 20));   // Independencia
        holidays.add(LocalDate.of(year, 8, 7));    // Batalla de Boyacá
        holidays.add(LocalDate.of(year, 12, 8));   // Inmaculada Concepción
        holidays.add(LocalDate.of(year, 12, 25));  // Navidad

        // ------------------ LEY EMILIANI ------------------
        holidays.add(applyEmiliani(LocalDate.of(year, 1, 6)));   // Reyes
        holidays.add(applyEmiliani(LocalDate.of(year, 3, 19)));  // San José
        holidays.add(applyEmiliani(LocalDate.of(year, 6, 29)));  // San Pedro y San Pablo
        holidays.add(applyEmiliani(LocalDate.of(year, 8, 15)));  // Asunción
        holidays.add(applyEmiliani(LocalDate.of(year, 10, 12))); // Día de la Raza
        holidays.add(applyEmiliani(LocalDate.of(year, 11, 1)));  // Todos los Santos
        holidays.add(applyEmiliani(LocalDate.of(year, 11, 11))); // Independencia Cartagena

        // ------------------ SEMANA SANTA ------------------
        LocalDate easter = calculateEasterSunday(year);

        holidays.add(easter.minusDays(3)); // Jueves Santo
        holidays.add(easter.minusDays(2)); // Viernes Santo

        // ------------------ FESTIVOS CATÓLICOS MÓVILES ------------------
        holidays.add(applyEmiliani(easter.plusDays(43))); // Ascensión
        holidays.add(applyEmiliani(easter.plusDays(64))); // Corpus Christi
        holidays.add(applyEmiliani(easter.plusDays(71))); // Sagrado Corazón

        return holidays;
    }




    /* -----------------------------------------------------------
     *   Utilidades
     * ----------------------------------------------------------- */

    public boolean isHoliday(LocalDate date) {
        return getColombianHolidays(date.getYear()).contains(date);
    }

    public boolean isWeekend(LocalDate date) {
        DayOfWeek d = date.getDayOfWeek();
        return d == DayOfWeek.SATURDAY || d == DayOfWeek.SUNDAY;
    }

    public boolean isBusinessDay(LocalDate date) {
        return !isWeekend(date) && !isHoliday(date);
    }

    public Set<LocalDate> getWorkDays(int year) {
        Set<LocalDate> result = new HashSet<>();
        Set<LocalDate> holidays = getColombianHolidays(year);

        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        while (!start.isAfter(end)) {
            if (!isWeekend(start) && !holidays.contains(start)) {
                result.add(start);
            }
            start = start.plusDays(1);
        }

        return result;
    }


    /* ------------------ Ley Emiliani ------------------ */
    private LocalDate applyEmiliani(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();

        return switch (day) {
            case TUESDAY -> date.plusDays(6);
            case WEDNESDAY -> date.plusDays(5);
            case THURSDAY -> date.plusDays(4);
            default -> date;
        };
    }

    /* ------------------ Algoritmo de Pascua ------------------ */
    private LocalDate calculateEasterSunday(int year) {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;

        int i1 = h + l - 7 * m + 114;
        int month = i1 / 31;
        int day = 1 + i1 % 31;

        return LocalDate.of(year, month, day);
    }


    /* -----------------------------------------------------------
     *   2. VERSIÓN SIMPLE UNIVERSAL (festivos fijos)
     * ----------------------------------------------------------- */
    public Set<LocalDate> getSimpleUniversalHolidays(int year) {
        Set<LocalDate> holidays = new HashSet<>();

        holidays.add(LocalDate.of(year, 1, 1));   // Año Nuevo
        holidays.add(LocalDate.of(year, 5, 1));   // Día del Trabajo
        holidays.add(LocalDate.of(year, 12, 25)); // Navidad

        return holidays;
    }
}