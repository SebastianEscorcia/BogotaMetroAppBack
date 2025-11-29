package com.sena.BogotaMetroApp.persistence.repository.interrupcion;

import com.sena.BogotaMetroApp.persistence.models.interrupcion.Interrupcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterrupcionRepository extends JpaRepository<Interrupcion, Long> {
}

