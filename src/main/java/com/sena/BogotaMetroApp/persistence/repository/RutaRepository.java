package com.sena.BogotaMetroApp.persistence.repository;

import com.sena.BogotaMetroApp.persistence.models.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RutaRepository extends JpaRepository<Ruta, Long> {
    List<Ruta> findByActivoTrue();
}
