package com.sena.BogotaMetroApp.mapper.qr;

import com.sena.BogotaMetroApp.presentation.dto.qr.QrRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.services.exception.pago.PagoException;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import com.sena.BogotaMetroApp.services.exception.viaje.ViajeException;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.Viaje;
import com.sena.BogotaMetroApp.persistence.models.pago.Pago;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.persistence.repository.ViajeRepository;
import com.sena.BogotaMetroApp.persistence.repository.pago.PagoRepository;
import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QrMapper {

    private final UsuarioRepository usuarioRepository;
    private final ViajeRepository viajeRepository;
    private final PagoRepository pagoRepository;

    /**
     * Convierte QrRequestDTO a entidad Qr (SIN PERSISTIR)
     */
    public Qr toEntity(QrRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_NOT_FOUND));

        Qr qr = new Qr();
        qr.setTipo(dto.getTipo());
        qr.setUsuario(usuario);
        qr.setFechaGeneracion(LocalDateTime.now());
        qr.setConsumido(false);
        qr.setContenidoQr(generarContenidoQr(dto, usuario));

        asignarRelaciones(qr, dto);

        return qr;
    }

    /**
     * Convierte entidad Qr a QrResponseDTO
     */
    public QrResponseDTO toDTO(Qr qr) {
        QrResponseDTO dto = new QrResponseDTO();
        dto.setId(qr.getId());
        dto.setTipo(qr.getTipo());
        dto.setContenidoQr(qr.getContenidoQr());
        dto.setFechaGeneracion(qr.getFechaGeneracion());
        return dto;
    }



    /**
     * Asigna las relaciones de pago o viaje según el tipo de QR
     */
    private void asignarRelaciones(Qr qr, QrRequestDTO dto) {
        if (dto.getTipo() == TipoQr.PAGO && dto.getIdPago() != null) {
            Pago pago = pagoRepository.findById(dto.getIdPago())
                    .orElseThrow(() -> new PagoException(ErrorCodeEnum.PAGO_NOT_FOUND));
            qr.setPago(pago);
        }

        if (dto.getTipo() == TipoQr.VIAJE && dto.getIdViaje() != null) {
            Viaje viaje = viajeRepository.findById(dto.getIdViaje())
                    .orElseThrow(() -> new ViajeException(ErrorCodeEnum.VIAJE_NOT_FOUND));
            qr.setViaje(viaje);
        }
    }

    /**
     * Genera el contenido único del QR
     */
    private String generarContenidoQr(QrRequestDTO dto, Usuario usuario) {
        String uuid = UUID.randomUUID().toString();
        String timestamp = String.valueOf(System.currentTimeMillis());

        return switch (dto.getTipo()) {
            case PAGO -> String.format("PAGO:%d:%d:%s:%s",
                    usuario.getId(), dto.getIdPago(), uuid, timestamp);
            case VIAJE -> String.format("VIAJE:%d:%d:%s:%s",
                    usuario.getId(), dto.getIdViaje(), uuid, timestamp);
        };
    }
}
