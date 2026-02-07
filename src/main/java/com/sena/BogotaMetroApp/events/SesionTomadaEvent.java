package com.sena.BogotaMetroApp.events;

public record SesionTomadaEvent(
        Long idSesion,
        Long idSoporte
) {}