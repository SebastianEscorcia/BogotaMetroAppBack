package com.sena.BogotaMetroApp.mapper.pago;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.transaccion.CobroPasaje;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Recarga;
import com.sena.BogotaMetroApp.persistence.repository.pasarela.PasarelaRepository;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import com.sena.BogotaMetroApp.utils.enums.MonedaEnum;
import org.springframework.stereotype.Component;

import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Transaccion;
import com.sena.BogotaMetroApp.persistence.models.pasarela.Pasarela;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TransaccionMapper {

    private final UsuarioRepository usuarioRepository;
    private final PasarelaRepository pasarelaRepository;

    /**
     *  Retorna la Recarga 'Pago' sin guardar.
     */
    public Recarga toRecargaEntity(TransaccionRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_NOT_FOUND));

        Pasarela pasarela = pasarelaRepository.findById(dto.getIdPasarela())
                .orElseThrow(() -> new RuntimeException("Pasarela no encontrada"));


        Recarga recarga = new Recarga();
        recarga.setUsuario(usuario);
        recarga.setValor(dto.getValorPagado());
        recarga.setDescripcion(dto.getDescripcion());
        recarga.setMoneda(dto.getMoneda() != null ? dto.getMoneda() : MonedaEnum.COP);
        recarga.setFecha(LocalDateTime.now());

        recarga.setPasarela(pasarela);
        recarga.setReferenciaPasarela(dto.getReferenciaPasarela());
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
            }
        }

        // 1. Si es una RECARGA
        if (transaccion instanceof Recarga recarga) {
            dto.setReferenciaPasarela(recarga.getReferenciaPasarela());
            dto.setMedioDePago(recarga.getMedioDePago());

            if (recarga.getPasarela() != null) {
                dto.setIdPasarela(recarga.getPasarela().getId());
                dto.setNombrePasarela(recarga.getPasarela().getNombre());
            }
        }
        // 2. Si es un COBRO DE PASAJE (Torniquete)
        else if (transaccion instanceof CobroPasaje cobro) {
            dto.setIdEstacion(cobro.getEstacionId());
        }

        return dto;
    }
}