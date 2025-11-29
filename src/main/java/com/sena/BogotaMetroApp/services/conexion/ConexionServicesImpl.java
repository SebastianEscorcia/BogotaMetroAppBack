package com.sena.BogotaMetroApp.services.conexion;

import com.sena.BogotaMetroApp.presentation.dto.conexion.ConexionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.conexion.ConexionResponseDTO;
import com.sena.BogotaMetroApp.mapper.conexion.ConexionMapper;
import com.sena.BogotaMetroApp.persistence.models.conexion.Conexion;
import com.sena.BogotaMetroApp.persistence.repository.conexion.ConexionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConexionServicesImpl implements IConexionService {
    private final ConexionRepository conexionRepository;
    private final ConexionMapper mapper;

    @Override
    public ConexionResponseDTO crear(ConexionRequestDTO dto) {
        if (dto.getIdOrigen() >= dto.getIdDestino()) {
            throw new IllegalArgumentException("id_origen debe ser menor que id_destino");
        }

        Conexion conexion = mapper.toEntity(dto);
        return mapper.toDTO(conexionRepository.save(conexion));
    }

    @Override
    public List<ConexionResponseDTO> listar() {
        return conexionRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}

