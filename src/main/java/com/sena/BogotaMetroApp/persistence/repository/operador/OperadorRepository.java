package com.sena.BogotaMetroApp.persistence.repository.operador;

import com.sena.BogotaMetroApp.persistence.models.operador.Operador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Long> {
    @Query("""
        SELECT o FROM Operador o
        JOIN FETCH o.usuario u
        JOIN FETCH u.datosPersonales
        JOIN FETCH u.rol
        WHERE u.correo = :correo AND u.activo = true
        """)
    Optional<Operador> findByUsuarioCorreo(@Param("correo") String correo);
    @Query("""
        SELECT o FROM Operador o
        JOIN FETCH o.usuario u
        JOIN FETCH u.datosPersonales
        JOIN FETCH u.rol
        WHERE o.id = :id AND u.activo = true
        """)
    Optional<Operador> findByIdWithFullData(@Param("id") Long id);

    @Query("""
        SELECT o FROM Operador o
        JOIN FETCH o.usuario u
        JOIN FETCH u.datosPersonales
        WHERE u.activo = true
        """)
    List<Operador> findAllActive();

    @Query("SELECT o FROM Operador o " +
            "JOIN o.usuario u " +
            "JOIN u.datosPersonales dp " +
            "WHERE u.activo = true " +
            "AND (:busqueda IS NULL OR :busqueda = '' OR " +
            "LOWER(dp.nombreCompleto) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "dp.numDocumento LIKE CONCAT('%', :busqueda, '%'))")
    List<Operador> buscarPorFiltro(@Param("busqueda") String busqueda);
}
