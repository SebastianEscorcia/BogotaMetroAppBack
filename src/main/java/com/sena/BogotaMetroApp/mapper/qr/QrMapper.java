package com.sena.BogotaMetroApp.mapper.qr;


import com.sena.BogotaMetroApp.presentation.dto.QrCacheDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;

import com.sena.BogotaMetroApp.persistence.models.qr.Qr;

import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QrMapper {
    /**
     * Convierte entidad Qr a QrResponseDTO
     */
    public QrResponseDTO toDTO(Qr qr) {
        QrResponseDTO dto = new QrResponseDTO();
        dto.setId(qr.getId());
        dto.setTipo(qr.getTipo());
        dto.setContenidoQr(qr.getContenidoQr());
        dto.setFechaGeneracion(qr.getFechaGeneracion());
        dto.setFechaExpiracion(qr.getFechaExpiracion());
        return dto;
    }


    /**
     * Genera el contenido único del QR
     */
    public String generarContenidoQr(TipoQr tipo, Long idUsuario, Long idReferencia) {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String timestamp = String.valueOf(System.currentTimeMillis());
        return switch (tipo) {
            case ACCESO -> String.format("ACCESO:%d:%d:%s:%s",
                    idUsuario, idReferencia, uuid, timestamp);
            case VIAJE -> String.format("VIAJE:%d:%d:%s:%s",
                    idUsuario, idReferencia, uuid, timestamp);
        };
    }


    public QrCacheDTO toCacheDTO(Qr qr) {
        return QrCacheDTO.builder()
                .id(qr.getId())
                .usuarioId(qr.getUsuario().getId())
                .contenido(qr.getContenidoQr())
                .tipoQr(qr.getTipo())
                .fechaGeneracion(qr.getFechaGeneracion())
                .fechaExpiracion(qr.getFechaExpiracion())
                .consumido(qr.getConsumido())
                .version(qr.getVersion() != null ? qr.getVersion() : 0L)
                .build();
    }

    public QrResponseDTO toDTOFromCache(QrCacheDTO cache) {
        QrResponseDTO dto = new QrResponseDTO();
        dto.setId(cache.getId());
        dto.setContenidoQr(cache.getContenido());
        dto.setTipo(cache.getTipoQr());
        dto.setFechaGeneracion(cache.getFechaGeneracion());
        dto.setFechaExpiracion(cache.getFechaExpiracion());
        return dto;
    }


}
