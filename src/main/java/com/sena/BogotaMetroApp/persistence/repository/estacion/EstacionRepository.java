package com.sena.BogotaMetroApp.persistence.repository.estacion;

import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstacionRepository extends JpaRepository<Estacion, Long> {
}
