package com.sena.BogotaMetroApp.persistence.repository.soporte;

import com.sena.BogotaMetroApp.persistence.models.soporte.Soporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoporteRepository extends JpaRepository<Soporte, Long> {

    @Query("SELECT s FROM Soporte s " +
            "JOIN s.usuario u " +
            "JOIN u.datosPersonales dp " +
            "WHERE u.activo = true " +
            "AND (:busqueda IS NULL OR :busqueda = '' OR " +
            "LOWER(dp.nombreCompleto) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "dp.numDocumento LIKE CONCAT('%', :busqueda, '%'))")
    List<Soporte> buscarPorFiltro(@Param("busqueda") String busqueda);
}
