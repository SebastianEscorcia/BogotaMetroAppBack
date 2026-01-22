package com.sena.BogotaMetroApp.persistence.repository.soporte;

import com.sena.BogotaMetroApp.persistence.models.soporte.Soporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SoporteRepository extends JpaRepository<Soporte, Long> {

    @Query("""
        SELECT s FROM Soporte s
        JOIN FETCH s.usuario u
        JOIN FETCH u.datosPersonales
        JOIN FETCH u.rol
        WHERE u.correo = :correo AND u.activo = true
        """)
    Optional<Soporte> findByUsuarioCorreo(@Param("correo") String correo);

    @Query("""
        SELECT s FROM Soporte s
        JOIN FETCH s.usuario u
        JOIN FETCH u.datosPersonales
        JOIN FETCH u.rol
        WHERE s.id = :id AND u.activo = true
        """)
    Optional<Soporte> findByIdWithFullData(@Param("id") Long id);
    @Query("""
        SELECT s FROM Soporte s
        JOIN FETCH s.usuario u
        JOIN FETCH u.datosPersonales
        WHERE u.activo = true
        """)
    List<Soporte> findAllActive();

    @Query("SELECT s FROM Soporte s " +
            "JOIN s.usuario u " +
            "JOIN u.datosPersonales dp " +
            "WHERE u.activo = true " +
            "AND (:busqueda IS NULL OR :busqueda = '' OR " +
            "LOWER(dp.nombreCompleto) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "dp.numDocumento LIKE CONCAT('%', :busqueda, '%'))")
    List<Soporte> buscarPorFiltro(@Param("busqueda") String busqueda);
}
