package com.sena.BogotaMetroApp.persistence.repository.pasajero;

import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {
    @Query("""
        SELECT p FROM Pasajero p
        JOIN FETCH p.usuario u
        JOIN FETCH u.datosPersonales
        JOIN FETCH u.rol
        LEFT JOIN FETCH p.tarjetaVirtual
        WHERE u.correo = :correo AND u.activo = true
        """)
    Optional<Pasajero> findByUsuarioCorreo(@Param("correo") String correo);
    @Query("""
        SELECT p FROM Pasajero p
        JOIN FETCH p.usuario u
        JOIN FETCH u.datosPersonales
        JOIN FETCH u.rol
        LEFT JOIN FETCH p.tarjetaVirtual
        WHERE p.id = :id AND u.activo = true
        """)
    Optional<Pasajero> findByIdWithFullData(@Param("id") Long id);
    @Query("""
        SELECT p FROM Pasajero p
        JOIN FETCH p.usuario u
        JOIN FETCH u.datosPersonales
        WHERE u.activo = true
        """)
    List<Pasajero> findAllActive();
    @Query("""
        SELECT p FROM Pasajero p
        JOIN p.usuario u
        JOIN u.datosPersonales dp
        WHERE u.activo = true
        AND (:busqueda IS NULL OR :busqueda = '' OR
            LOWER(dp.nombreCompleto) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR
            dp.numDocumento LIKE CONCAT('%', :busqueda, '%'))
        """)
    List<Pasajero> buscarPorFiltro(@Param("busqueda") String busqueda);
}
