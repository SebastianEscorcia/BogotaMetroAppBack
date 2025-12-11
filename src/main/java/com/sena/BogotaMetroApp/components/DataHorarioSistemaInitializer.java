package com.sena.BogotaMetroApp.components;

import com.sena.BogotaMetroApp.persistence.models.horariosistema.HorarioSistema;
import com.sena.BogotaMetroApp.persistence.repository.horariosistema.HorarioSistemaRepository;
import com.sena.BogotaMetroApp.utils.enums.DiaSemana;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataHorarioSistemaInitializer implements CommandLineRunner {

    private final HorarioSistemaRepository horarioSistemaRepository;

    private final Logger log = LoggerFactory.getLogger(DataHorarioSistemaInitializer.class);

    // Valores por defecto, pueden sobrescribirse en application.properties
    @Value("${app.horario.inicio:04:00}")
    private String defaultHoraInicio;

    @Value("${app.horario.fin:23:00}")
    private String defaultHoraFin;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Inicializando horarios del sistema (idempotente)...");

        for (DiaSemana dia : DiaSemana.values()) {

            if (horarioSistemaRepository.findByDiaAndActivoTrue(dia).isEmpty()) {
                inicializarHorarios(dia);
            } else {
                log.debug("Ya existe horario activo para {}", dia);
            }
        }

        log.info("Inicialización de horarios completada.");
    }

    private void inicializarHorarios(DiaSemana dia) {
        var horario = new HorarioSistema();
        horario.setDia(dia);
        horario.setHoraInicio(LocalTime.parse(defaultHoraInicio));
        horario.setHoraFin(LocalTime.parse(defaultHoraFin));
        horario.setActivo(true);

        horarioSistemaRepository.save(horario);
        log.info("Se creó horario por defecto para {}: {} - {}", dia, defaultHoraInicio, defaultHoraFin);
    }
}
