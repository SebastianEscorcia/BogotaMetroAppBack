package com.sena.BogotaMetroApp.persistence.repository.linea;

import com.sena.BogotaMetroApp.persistence.models.Linea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineaRepository extends JpaRepository<Linea, Long> {
    List<Linea> findByActivoTrue();
}
