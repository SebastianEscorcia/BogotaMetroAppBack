package com.sena.BogotaMetroApp.services.pasarela;

import com.sena.BogotaMetroApp.presentation.dto.pasarela.PasarelaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasarela.PasarelaResponseDTO;
import com.sena.BogotaMetroApp.mapper.pasarela.PasarelaMapper;
import com.sena.BogotaMetroApp.persistence.models.pasarela.Pasarela;
import com.sena.BogotaMetroApp.persistence.repository.pasarela.PasarelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasarelaServiceImpl implements IPasarelaService {

    private final PasarelaMapper pasarelaMapper;
    private final PasarelaRepository pasarelaRepository;


    @Override
    @Transactional
    public PasarelaResponseDTO crearPasarela(PasarelaRequestDTO dto) {
        return pasarelaMapper.toEntity(dto);
    }

    @Override
    public PasarelaResponseDTO obtenerPasarelaPorId(Long id) {
        Pasarela pasarela = pasarelaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pasarela no encontrada"));
        return pasarelaMapper.toDTO(pasarela);
    }

    @Override
    public List<PasarelaResponseDTO> obtenerTodasLasPasarelas() {
        return pasarelaRepository.findAll()
                .stream()
                .map(pasarelaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PasarelaResponseDTO obtenerPasarelaPorNombre(String nombre) {
        Pasarela pasarela = pasarelaRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Pasarela no encontrada con ese nombre"));
        return pasarelaMapper.toDTO(pasarela);
    }

    @Override
    public PasarelaResponseDTO obtenerPasarelaPorCodigo(Integer codigo) {
        Pasarela pasarela = pasarelaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Pasarela no encontrada con ese código"));
        return pasarelaMapper.toDTO(pasarela);
    }

    @Override
    public List<PasarelaResponseDTO> obtenerPasarelasPorPais(String pais) {
        return pasarelaRepository.findByPais(pais)
                .stream()
                .map(pasarelaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PasarelaResponseDTO actualizarPasarela(Long id, PasarelaRequestDTO dto) {
        return pasarelaMapper.updateEntity(id, dto);
    }

    @Override
    @Transactional
    public void eliminarPasarela(Long id) {
        if (!pasarelaRepository.existsById(id)) {
            throw new RuntimeException("Pasarela no encontrada");
        }
        pasarelaRepository.deleteById(id);
    }
}
