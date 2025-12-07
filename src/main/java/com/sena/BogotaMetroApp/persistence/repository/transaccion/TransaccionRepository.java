package com.sena.BogotaMetroApp.persistence.repository.transaccion;

import com.sena.BogotaMetroApp.persistence.models.transaccion.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    // Buscar pagos por usuario
    List<Transaccion> findByUsuarioId(Long idUsuario);


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

}
