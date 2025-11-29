package com.sena.BogotaMetroApp.mapper.pasarela;

import com.sena.BogotaMetroApp.presentation.dto.pasarela.PasarelaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasarela.PasarelaResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.pasarela.Pasarela;
import com.sena.BogotaMetroApp.persistence.repository.pasarela.PasarelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasarelaMapper {

    private final PasarelaRepository pasarelaRepository;

    /**
     * Convierte PasarelaRequestDTO a entidad Pasarela y lo persiste
     */
    public PasarelaResponseDTO toEntity(PasarelaRequestDTO dto) {
        // Validar nombre único
        if (pasarelaRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe una pasarela con ese nombre");
        }

        // Validar código único
        if (pasarelaRepository.existsByCodigo(dto.getCodigo())) {
            throw new RuntimeException("Ya existe una pasarela con ese código");
        }

        Pasarela pasarela = new Pasarela();
        pasarela.setNombre(dto.getNombre());
        pasarela.setCodigo(dto.getCodigo());
        pasarela.setPais(dto.getPais());

        pasarelaRepository.save(pasarela);
        return toDTO(pasarela);
    }

    /**
     * Convierte entidad Pasarela a PasarelaResponseDTO
     */
    public PasarelaResponseDTO toDTO(Pasarela pasarela) {
        PasarelaResponseDTO dto = new PasarelaResponseDTO();
        dto.setId(pasarela.getId());
        dto.setNombre(pasarela.getNombre());
        dto.setCodigo(pasarela.getCodigo());
        dto.setPais(pasarela.getPais());
        return dto;
    }

    /**
     * Actualiza una entidad Pasarela existente
     */
    public PasarelaResponseDTO updateEntity(Long id, PasarelaRequestDTO dto) {
        Pasarela pasarela = pasarelaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pasarela no encontrada"));

        // Validar nombre único (excepto si es el mismo)
        if (!pasarela.getNombre().equals(dto.getNombre()) &&
                pasarelaRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe una pasarela con ese nombre");
        }

        // Validar código único (excepto si es el mismo)
        if (!pasarela.getCodigo().equals(dto.getCodigo()) &&
                pasarelaRepository.existsByCodigo(dto.getCodigo())) {
            throw new RuntimeException("Ya existe una pasarela con ese código");
        }

        pasarela.setNombre(dto.getNombre());
        pasarela.setCodigo(dto.getCodigo());
        pasarela.setPais(dto.getPais());

        pasarelaRepository.save(pasarela);
        return toDTO(pasarela);
    }
}
