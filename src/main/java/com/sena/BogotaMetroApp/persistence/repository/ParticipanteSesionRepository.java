package com.sena.BogotaMetroApp.persistence.repository;

import com.sena.BogotaMetroApp.persistence.models.sesionchat.ParticipanteSesion;
import com.sena.BogotaMetroApp.persistence.models.sesionchat.SesionChat;
import com.sena.BogotaMetroApp.utils.enums.EstadoSesionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipanteSesionRepository extends JpaRepository<ParticipanteSesion, Long> {

    boolean existsBySesionChatIdAndUsuarioIdAndActivoTrue(Long idSesion, Long idUsuario);

    @Query("SELECT p.sesionChat FROM ParticipanteSesion p " +
            "WHERE p.usuario.id = :idUsuario " +
            "AND p.sesionChat.estado IN :estados " +
            "AND p.activo = true")
    Optional<SesionChat> findSesionActivaPorUsuario(
            @Param("idUsuario") Long idUsuario,
            @Param("estados") List<EstadoSesionEnum> estados);

    @Query("SELECT p.sesionChat FROM ParticipanteSesion p " +
            "WHERE p.usuario.id = :idSoporte " +
            "AND p.sesionChat.estado IN :estados " +
            "AND p.activo = true")
    List<SesionChat> findSesionesActivasPorUsuario(
            @Param("idSoporte") Long idSoporte,
            @Param("estados") List<EstadoSesionEnum> estados);

    @Query("""
        SELECT DISTINCT s FROM SesionChat s
        JOIN FETCH s.participantes p
        JOIN FETCH p.usuario u
        JOIN FETCH u.rol
        LEFT JOIN FETCH u.datosPersonales
        WHERE s.id IN (
            SELECT ps.sesionChat.id FROM ParticipanteSesion ps 
            WHERE ps.usuario.id = :idSoporte 
            AND ps.sesionChat.estado IN :estados 
            AND ps.activo = true
        )
    """)
    List<SesionChat> findSesionesActivasPorUsuarioOptimizado(
            @Param("idSoporte") Long idSoporte,
            @Param("estados") List<EstadoSesionEnum> estados);
}
