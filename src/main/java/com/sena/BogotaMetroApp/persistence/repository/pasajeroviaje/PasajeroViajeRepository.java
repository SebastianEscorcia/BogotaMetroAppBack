package com.sena.BogotaMetroApp.persistence.repository.pasajeroviaje;

import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViaje;
import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViajeId;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasajeroViajeRepository extends JpaRepository<@NotNull PasajeroViaje, @NotNull PasajeroViajeId> {
    List<PasajeroViaje> findByPasajeroId(Long idPasajero);

    List<PasajeroViaje> findByViajeId(Long idViaje);

    boolean existsByIdPasajeroIdAndIdViajeId(Long pasajeroId, Long viajeId);
}
