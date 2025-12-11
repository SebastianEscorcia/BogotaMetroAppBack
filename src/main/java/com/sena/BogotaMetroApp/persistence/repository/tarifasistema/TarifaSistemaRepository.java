package com.sena.BogotaMetroApp.persistence.repository.tarifasistema;

import com.sena.BogotaMetroApp.persistence.models.tarifasistema.TarifaSistema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TarifaSistemaRepository extends JpaRepository<TarifaSistema, Long> {
    Optional<TarifaSistema> findByActivaTrue();
}
