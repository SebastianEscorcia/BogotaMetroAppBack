package com.sena.BogotaMetroApp.persistence.repository.qr;

import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QrRepository extends JpaRepository<Qr, Long> {

    Optional<Qr> findByContenidoQr(String contenidoQr);

    Optional<Qr> findByContenidoQrAndUsuarioId(String contenidoQr, Long usuarioId);

    @Query("SELECT q FROM Qr q WHERE q.id = :id AND q.fechaGeneracion >= :fechaMin")
    Optional<Qr> findValidQr(@Param("id") Long id, @Param("fechaMin") LocalDateTime fechaMin);

    @Query("SELECT q FROM Qr q JOIN FETCH q.usuario WHERE q.contenidoQr = :contenido")
    Optional<Qr> findByContenidoQrWithUsuario(@Param("contenido") String contenido);

    List<Qr> findByTransaccionIdAndTipo(Long idTransaccion, TipoQr tipo);

    @Query("SELECT q FROM Qr q WHERE q.transaccion.id = :idTransaccion AND q.tipo = 'VIAJE' ORDER BY q.fechaGeneracion DESC")
    List<Qr> findQrsByTransaccionOrderByFechaDesc(@Param("idTransaccion") Long idTransaccion);

    List<Qr> findByUsuarioId(Long usuarioId);

    List<Qr> findByViajeId(Long idViaje);

    List<Qr> findByConsumido(Boolean consumido);

    List<Qr> findByTipo(TipoQr tipo);

    @Query("SELECT q FROM Qr q WHERE q.fechaGeneracion < :fechaLimite AND q.consumido = false")
    List<Qr> findQrsExpirados(@Param("fechaLimite") LocalDateTime fechaLimite);
}
