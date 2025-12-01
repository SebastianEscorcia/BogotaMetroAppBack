package com.sena.BogotaMetroApp.mapper.pago;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.repository.pasarela.PasarelaRepository;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import org.springframework.stereotype.Component;

import com.sena.BogotaMetroApp.presentation.dto.pago.PagoRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pago.PagoResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.pago.Pago;
import com.sena.BogotaMetroApp.persistence.models.pasarela.Pasarela;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PagoMapper {

    private final UsuarioRepository usuarioRepository;
    private final PasarelaRepository pasarelaRepository;

    /**
     *  Retorna la Entidad 'Pago' sin guardar.
     */
    public Pago toEntity(PagoRequestDTO dto) {
        // Validar existencia de entidades relacionadas es aceptable en el Mapper
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_NOT_FOUND));

        Pasarela pasarela = pasarelaRepository.findById(dto.getIdPasarela())
                .orElseThrow(() -> new RuntimeException("Pasarela no encontrada"));



        Pago pago = new Pago();
        pago.setUsuario(usuario);
        pago.setPasarela(pasarela);
        pago.setValorPagado(dto.getValorPagado());
        pago.setDescripcion(dto.getDescripcion());
        pago.setReferenciaPasarela(dto.getReferenciaPasarela());
        pago.setMoneda(dto.getMoneda() != null ? dto.getMoneda() : "COP");
        pago.setMedioDePago(dto.getMedioDePago());


        pago.setFechaPago(LocalDateTime.now());

        return pago;
    }

    /**
     * Convierte entidad Pago a PagoResponseDTO
     */
    public PagoResponseDTO toDTO(Pago pago) {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setId(pago.getId());
        dto.setValorPagado(pago.getValorPagado());
        dto.setFechaPago(pago.getFechaPago());
        dto.setDescripcion(pago.getDescripcion());
        dto.setReferenciaPasarela(pago.getReferenciaPasarela());
        dto.setMoneda(pago.getMoneda());
        dto.setMedioDePago(pago.getMedioDePago());

        if (pago.getUsuario() != null) {
            dto.setIdUsuario(pago.getUsuario().getId());
            // Manejo de null pointer si datos personales es null
            if (pago.getUsuario().getDatosPersonales() != null) {
                dto.setNombreUsuario(pago.getUsuario().getDatosPersonales().getNombreCompleto());
            }
        }

        if (pago.getPasarela() != null) {
            dto.setIdPasarela(pago.getPasarela().getId());
            dto.setNombrePasarela(pago.getPasarela().getNombre());
        }

        return dto;
    }
}