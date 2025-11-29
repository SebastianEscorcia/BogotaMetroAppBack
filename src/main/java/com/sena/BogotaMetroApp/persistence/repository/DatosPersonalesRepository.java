package com.sena.BogotaMetroApp.persistence.repository;

import com.sena.BogotaMetroApp.persistence.models.DatosPersonales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatosPersonalesRepository extends JpaRepository<DatosPersonales, Long> {
}
