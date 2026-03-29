package com.sena.BogotaMetroApp.persistence.repository;

import com.sena.BogotaMetroApp.persistence.models.sesionchat.SesionChat;
import com.sena.BogotaMetroApp.utils.enums.EstadoSesionEnum;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SesionChatRepository extends JpaRepository<SesionChat, Long> {

    @Query("SELECT s FROM SesionChat s JOIN s.participantes p " +
            "WHERE p.usuario.id = :idUsuario " +
            "AND s.estado IN (:estados) " +
            "AND p.activo = true")
    Optional<SesionChat> findSesionActivaPorUsuario(@Param("idUsuario") Long idUsuario,
                                                    @Param("estados") List<EstadoSesionEnum> estados);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SesionChat s WHERE s.id = :id")
    Optional<SesionChat> findByIdConBloqueo(@Param("id") Long id);

    @Query("""
        SELECT DISTINCT s FROM SesionChat s
        LEFT JOIN FETCH s.participantes p
        LEFT JOIN FETCH p.usuario u
        LEFT JOIN FETCH u.rol
        LEFT JOIN FETCH u.datosPersonales
        WHERE s.estado = :estado
        ORDER BY s.fechaUltimaActividad DESC
    """)
    List<SesionChat> findByEstadoWithParticipantes(@Param("estado") EstadoSesionEnum estado);

    // Ver la cola de espera
    // Trae todos los chats que nadie ha atendido aún.
    List<SesionChat> findByEstado(EstadoSesionEnum estado);

    // Trae los chats que este soporte específico está atendiendo ahora mismo.
    @Query("SELECT s FROM SesionChat s JOIN s.participantes p " +
            "WHERE p.usuario.id = :idUsuario " +
            "AND s.estado = :estado " +
            "AND p.activo = true")
    List<SesionChat> findMisChatsPorEstado(@Param("idUsuario") Long idUsuario,
                                           @Param("estado") EstadoSesionEnum estado);

    // Ver chats viejos de un pasajero

    @Query("SELECT s FROM SesionChat s JOIN s.participantes p " +
            "WHERE p.usuario.id = :idUsuario")
    List<SesionChat> findHistorialPorUsuario(@Param("idUsuario") Long idUsuario);

}


