package com.sena.BogotaMetroApp.persistence.repository.transaccion;

import com.sena.BogotaMetroApp.persistence.models.transaccion.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    // Buscar pagos por usuario
    List<Transaccion> findByUsuarioId(Long idUsuario);
    List<Transaccion> findByUsuarioDatosPersonalesNumDocumento(String numDocumento);
    List<Transaccion> findByUsuarioDatosPersonalesNombreCompleto(String nombre);

    @Query("SELECT t FROM Transaccion t WHERE t.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Transaccion> findByFechaBetween(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    // Común para todos: Usuario y fechas
    @Query("SELECT t FROM Transaccion t WHERE t.usuario.id = :idUsuario AND t.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Transaccion> findByUsuarioAndFechas(
            @Param("idUsuario") Long idUsuario,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    // Filtrar por usuario y rango de dinero
    List<Transaccion> findByUsuarioIdAndValorBetween(Long idUsuario, BigDecimal min, BigDecimal max);

    //Filtrar por usuario, rango de fechas y rango de dinero (El filtro definitivo)
    @Query("SELECT t FROM Transaccion t WHERE t.usuario.id = :idUsuario " +
            "AND t.fecha BETWEEN :inicio AND :fin " +
            "AND t.valor BETWEEN :min AND :max")
    List<Transaccion> findByFiltrosCombinados(
            @Param("idUsuario") Long idUsuario,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("min") BigDecimal min,
            @Param("max") BigDecimal max
    );

}
