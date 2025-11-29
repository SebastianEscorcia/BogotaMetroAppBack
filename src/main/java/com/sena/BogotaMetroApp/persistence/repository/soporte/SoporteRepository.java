package com.sena.BogotaMetroApp.persistence.repository.soporte;

import com.sena.BogotaMetroApp.persistence.models.soporte.Soporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoporteRepository extends JpaRepository<Soporte, Long> {}
