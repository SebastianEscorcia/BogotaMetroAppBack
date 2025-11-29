package com.sena.BogotaMetroApp.mapper.pago;

import com.sena.BogotaMetroApp.persistence.repository.pasarela.PasarelaRepository;
import org.springframework.stereotype.Component;

import com.sena.BogotaMetroApp.presentation.dto.pago.PagoRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pago.PagoResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.pago.Pago;
import com.sena.BogotaMetroApp.persistence.models.pasarela.Pasarela;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;

import com.sena.BogotaMetroApp.persistence.repository.pago.PagoRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PagoMapper {

    private final PagoRepository pagoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasarelaRepository pasarelaRepository;

    /**
     * Convierte PagoRequestDTO a entidad Pago y lo persiste
     */
    public PagoResponseDTO toEntity(PagoRequestDTO dto) {
        // Validar usuario
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar pasarela
        Pasarela pasarela = pasarelaRepository.findById(dto.getIdPasarela())
                .orElseThrow(() -> new RuntimeException("Pasarela no encontrada"));

        // Validar referencia única
        if (pagoRepository.findByReferenciaPasarela(dto.getReferenciaPasarela()).isPresent()) {
            throw new RuntimeException("Ya existe un pago con esta referencia");
        }

        Pago pago = new Pago();
        pago.setUsuario(usuario);
        pago.setPasarela(pasarela);
        pago.setValorPagado(dto.getValorPagado());
        pago.setDescripcion(dto.getDescripcion());
        pago.setReferenciaPasarela(dto.getReferenciaPasarela());
        pago.setMoneda(dto.getMoneda() != null ? dto.getMoneda() : "COP");
        pago.setMedioDePago(dto.getMedioDePago());
        pago.setFechaPago(LocalDateTime.now());

        pagoRepository.save(pago);
        return toDTO(pago);
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
            dto.setNombreUsuario(pago.getUsuario().getDatosPersonales().getNombreCompleto());
        }

        if (pago.getPasarela() != null) {
            dto.setIdPasarela(pago.getPasarela().getId());
            dto.setNombrePasarela(pago.getPasarela().getNombre());
        }

        return dto;
    }
}
