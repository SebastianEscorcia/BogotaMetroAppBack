package com.sena.BogotaMetroApp.persistence.repository.pasajero;

import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {
}
