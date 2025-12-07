package com.sena.BogotaMetroApp.persistence.repository;

import com.sena.BogotaMetroApp.persistence.models.sesionchat.SesionChat;
import com.sena.BogotaMetroApp.utils.enums.EstadoSesionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SesionChatRepository extends JpaRepository<SesionChat, Long> {

    @Query("SELECT s FROM SesionChat s JOIN s.participantes p " +
            "WHERE p.usuario.id = :idUsuario " +
            "AND s.estado IN (:estados) " +
            "AND p.activo = true")
    Optional<SesionChat> findSesionActivaPorUsuario(@Param("idUsuario") Long idUsuario,
                                                    @Param("estados") List<EstadoSesionEnum> estados);

    // 2. PARA EL SOPORTE (DASHBOARD): Ver la cola de espera
    // Trae todos los chats que nadie ha atendido aún.
    List<SesionChat> findByEstado(EstadoSesionEnum estado);

    // Trae los chats que este soporte específico está atendiendo ahora mismo.
    @Query("SELECT s FROM SesionChat s JOIN s.participantes p " +
            "WHERE p.usuario.id = :idUsuario " +
            "AND s.estado = :estado " +
            "AND p.activo = true")
    List<SesionChat> findMisChatsPorEstado(@Param("idUsuario") Long idUsuario,
                                           @Param("estado") EstadoSesionEnum estado);

    // 4. HISTORIAL: (Opcional) Ver chats viejos de un pasajero

    @Query("SELECT s FROM SesionChat s JOIN s.participantes p " +
            "WHERE p.usuario.id = :idUsuario")
    List<SesionChat> findHistorialPorUsuario(@Param("idUsuario") Long idUsuario);

    // Buscar sesiones ACTIVAS cuya última actividad fue ANTES de la fecha límite
    @Query("SELECT s FROM SesionChat s WHERE s.estado = 'ACTIVO' AND s.fechaUltimaActividad < :fechaLimite")
    List<SesionChat> buscarSesionesInactivas(@Param("fechaLimite") LocalDateTime fechaLimite);
}


