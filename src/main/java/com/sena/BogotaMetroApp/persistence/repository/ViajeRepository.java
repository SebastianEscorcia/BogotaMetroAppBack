package com.sena.BogotaMetroApp.persistence.repository;

import com.sena.BogotaMetroApp.persistence.models.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {
}
