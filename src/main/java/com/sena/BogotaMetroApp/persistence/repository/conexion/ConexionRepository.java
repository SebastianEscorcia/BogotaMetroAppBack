package com.sena.BogotaMetroApp.persistence.repository.conexion;

import com.sena.BogotaMetroApp.persistence.models.conexion.Conexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConexionRepository extends JpaRepository<Conexion, Long> {
    List<Conexion> findByActivoTrue();
}
