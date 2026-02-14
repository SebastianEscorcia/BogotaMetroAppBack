package com.sena.BogotaMetroApp.mapper.pago;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.models.transaccion.CobroPasaje;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Recarga;
import com.sena.BogotaMetroApp.persistence.repository.TarjetaVirtualRepository;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import com.sena.BogotaMetroApp.utils.enums.MonedaEnum;
import org.springframework.stereotype.Component;

import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Transaccion;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TransaccionMapper {

    private final UsuarioRepository usuarioRepository;
    private final TarjetaVirtualRepository tarjetaVirtualRepository;

    /**
     * Retorna la entidad Recarga lista para persistir.
     *
     * NOTA IMPORTANTE sobre diseño Event-Driven:
     * La tarjeta se asocia aquí porque es REQUERIDA por la BD para guardar la transacción.
     * Esto NO viola SRP porque asociar la tarjeta es parte de "crear la transacción".
     *
     * La ACTUALIZACIÓN del saldo (lógica de negocio) sí se delega al listener
     * RecargaRegistradaListener, manteniendo el desacoplamiento.
     */
    public Recarga toRecargaEntity(TransaccionRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_NOT_FOUND));

        // Obtener la tarjeta del usuario - necesaria para persistir la transacción
        TarjetaVirtual tarjeta = tarjetaVirtualRepository.findByPasajeroUsuarioId(dto.getIdUsuario())
                .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_DONT_CARD_ACTIVE));

        Recarga recarga = new Recarga();
        recarga.setUsuario(usuario);
        recarga.setTarjetaVirtual(tarjeta);  // Asociar la tarjeta para cumplir constraint de BD
        recarga.setValor(dto.getValorPagado());
        recarga.setDescripcion("Recarga de saldo");
        recarga.setMoneda(dto.getMoneda() != null ? dto.getMoneda() : MonedaEnum.COP);
        recarga.setFecha(LocalDateTime.now());

        recarga.setMedioDePago(dto.getMedioDePago());

        return recarga;
    }

    /**
     * Convierte entidad Transaccion (Polimórfica) a TransaccionResponseDTO
     */
    public TransaccionResponseDTO toDTO(Transaccion transaccion) {
        TransaccionResponseDTO dto = new TransaccionResponseDTO();
        dto.setId(transaccion.getId());
        dto.setValorPagado(transaccion.getValor());
        dto.setFechaPago(transaccion.getFecha());
        dto.setDescripcion(transaccion.getDescripcion());
        dto.setMoneda(transaccion.getMoneda());

        if (transaccion.getUsuario() != null) {
            dto.setIdUsuario(transaccion.getUsuario().getId());
            if (transaccion.getUsuario().getDatosPersonales() != null) {
                dto.setNombreUsuario(transaccion.getUsuario().getDatosPersonales().getNombreCompleto());
                dto.setNumDocumentoUsuario(transaccion.getUsuario().getDatosPersonales().getNumDocumento());
            }
        }

        //  Si es una RECARGA
        if (transaccion instanceof Recarga recarga) {
            dto.setMedioDePago(recarga.getMedioDePago());
        }
        //  Si es un COBRO DE PASAJE (Torniquete)
        else if (transaccion instanceof CobroPasaje cobro) {
            dto.setIdEstacion(cobro.getEstacionId());
        }

        return dto;
    }
}