package com.sena.BogotaMetroApp.persistence.repository.pago;

import com.sena.BogotaMetroApp.persistence.models.pago.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    // Buscar pagos por usuario
    List<Pago> findByUsuarioId(Long idUsuario);

    // Buscar por referencia de pasarela
    Optional<Pago> findByReferenciaPasarela(String referenciaPasarela);

    // Buscar pagos por pasarela
    List<Pago> findByPasarelaId(Long idPasarela);

    // Buscar pagos en un rango de fechas
    @Query("SELECT p FROM Pago p WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin")
    List<Pago> findByFechaPagoBetween(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    // Buscar pagos por usuario y rango de fechas
    @Query("SELECT p FROM Pago p WHERE p.usuario.id = :idUsuario AND p.fechaPago BETWEEN :fechaInicio AND :fechaFin")
    List<Pago> findByUsuarioAndFechas(
            @Param("idUsuario") Long idUsuario,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}
