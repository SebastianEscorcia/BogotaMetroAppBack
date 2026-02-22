package com.sena.BogotaMetroApp.persistence.repository.interrupcion;

import com.sena.BogotaMetroApp.persistence.models.interrupcion.Interrupcion;
import com.sena.BogotaMetroApp.utils.enums.EstadoInterrupcionEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterrupcionRepository extends JpaRepository<Interrupcion, Long> {

    List<Interrupcion> findByActivoTrue();

    /**
     * Verifica si existe una interrupción activa para una estación específica
     */
    boolean existsByEstacionIdAndActivoTrueAndEstado(Long estacionId, EstadoInterrupcionEnum estado);
}

