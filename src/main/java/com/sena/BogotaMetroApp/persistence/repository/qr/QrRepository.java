package com.sena.BogotaMetroApp.persistence.repository.qr;

import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QrRepository extends JpaRepository<Qr, Long> {

    Optional<Qr> findByContenidoQr(String contenidoQr);

    // Buscar QR activo de un usuario (para no generar múltiples si ya tiene uno válido)
    @Query("SELECT q FROM Qr q WHERE q.usuario.id = :usuarioId AND q.consumido = false AND q.fechaExpiracion > :ahora")
    Optional<Qr> findQrActivoByUsuario(@Param("usuarioId") Long usuarioId, @Param("ahora") LocalDateTime ahora);


    List<Qr> findByUsuarioId(Long usuarioId);

    // Para limpieza automática (Cron job)
    @Query("SELECT q FROM Qr q WHERE q.fechaExpiracion < :fechaLimite")
    List<Qr> findQrsExpirados(@Param("fechaLimite") LocalDateTime fechaLimite);

    // Consulta con bloqueo para evitar condiciones de carrera al consumir un QR
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT q FROM Qr q WHERE q.usuario.id = :usuarioId AND q.consumido = false ORDER BY q.fechaGeneracion DESC")
    Optional<Qr> findLatestNotConsumedQrForUserForUpdate(@Param("usuarioId") Long usuarioId);

    Optional<Qr> findFirstByUsuarioIdAndConsumidoFalseOrderByFechaGeneracionDesc(Long usuarioId);
}
