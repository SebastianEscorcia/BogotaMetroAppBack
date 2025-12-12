package com.sena.BogotaMetroApp.persistence.repository.operador;

import com.sena.BogotaMetroApp.persistence.models.operador.Operador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Long> {

    @Query("SELECT o FROM Operador o " +
            "JOIN o.usuario u " +
            "JOIN u.datosPersonales dp " +
            "WHERE u.activo = true " +
            "AND (:busqueda IS NULL OR :busqueda = '' OR " +
            "LOWER(dp.nombreCompleto) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "dp.numDocumento LIKE CONCAT('%', :busqueda, '%'))")
    List<Operador> buscarPorFiltro(@Param("busqueda") String busqueda);
}
