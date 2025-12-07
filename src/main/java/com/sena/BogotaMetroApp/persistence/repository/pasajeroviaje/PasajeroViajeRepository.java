package com.sena.BogotaMetroApp.persistence.repository.pasajeroviaje;

import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViaje;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasajeroViajeRepository extends JpaRepository<@NotNull PasajeroViaje,Long> {
    List<PasajeroViaje> findByPasajeroId(Long idPasajero);

    List<PasajeroViaje> findByViajeId(Long idViaje);

    List<PasajeroViaje> findByPasajeroIdAndViajeId(Long pasajeroId, Long viajeId);
}
