package com.sena.BogotaMetroApp.services.puntointeres;

import com.sena.BogotaMetroApp.presentation.dto.puntointeres.PuntoInteresRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.puntointeres.PuntoInteresResponseDTO;
import com.sena.BogotaMetroApp.mapper.puntointeres.PuntoInteresMapper;
import com.sena.BogotaMetroApp.persistence.models.puntointeres.PuntoInteres;
import com.sena.BogotaMetroApp.persistence.repository.puntointeres.PuntoInteresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PuntoInteresServicesImpl implements IPuntoInteresService {
    private final PuntoInteresRepository poiRepository;
    private final PuntoInteresMapper mapper;

    @Override
    public PuntoInteresResponseDTO crear(PuntoInteresRequestDTO dto) {
        PuntoInteres p = mapper.toEntity(dto);
        return mapper.toDTO(poiRepository.save(p));
    }

    @Override
    public List<PuntoInteresResponseDTO> listar() {
        return poiRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}
