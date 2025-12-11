package com.sena.BogotaMetroApp.utils.holders;


import com.sena.BogotaMetroApp.utils.logic.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Bridge class to access HolidayService methods statically.
 * Patrón estructural Bridge: Separa la abstracción (HolidayBridge) de su implementación (HolidayService)
 * para que ambas puedan variar independientemente.
 */
@Component
public class HolidayBridge {

    private static HolidayService holidayService;

    @Autowired
    public HolidayBridge(HolidayService service) {
        holidayService = service;
    }

    public static boolean isHoliday(LocalDate date) {
        return holidayService.isHoliday(date);
    }
}
