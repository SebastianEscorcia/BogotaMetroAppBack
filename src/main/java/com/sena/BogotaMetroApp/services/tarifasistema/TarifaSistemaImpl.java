package com.sena.BogotaMetroApp.services.tarifasistema;
import com.sena.BogotaMetroApp.services.exception.tarifasistema.TarifaSistemaException;
import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.mapper.tarifasistema.TarifaSistemaMapper;
import com.sena.BogotaMetroApp.persistence.models.tarifasistema.TarifaSistema;
import com.sena.BogotaMetroApp.persistence.repository.tarifasistema.TarifaSistemaRepository;
import com.sena.BogotaMetroApp.presentation.dto.tarifasistema.TarifaSistemaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.tarifasistema.TarifaSistemaResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TarifaSistemaImpl implements  ITarifaSistemaService{
    private final TarifaSistemaRepository tarifaSistemaRepository;
    private final TarifaSistemaMapper mapper;
    @Override
    public TarifaSistemaResponseDTO crearTarifa(TarifaSistemaRequestDTO request) {
        tarifaSistemaRepository.findByActivaTrue().ifPresent(t -> {
            t.setActiva(false);
            tarifaSistemaRepository.save(t);
        });

        TarifaSistema tarifa = mapper.toEntity(request);
        tarifa.setActiva(true);
        TarifaSistema guardada = tarifaSistemaRepository.save(tarifa);
        return mapper.toDTO(guardada);

    }

    @Override
    public TarifaSistemaResponseDTO obtenerTarifaActiva() {
        TarifaSistema tarifa = tarifaSistemaRepository.findByActivaTrue()
                .orElseThrow(() -> new TarifaSistemaException(ErrorCodeEnum.TARIFA_NOT_FOUND));
        return mapper.toDTO(tarifa);
    }

    @Override
    public TarifaSistemaResponseDTO actualizarTarifa(Long id, TarifaSistemaRequestDTO request) {
        TarifaSistema tarifa = tarifaSistemaRepository.findById(id)
                .orElseThrow(() -> new TarifaSistemaException(ErrorCodeEnum.TARIFA_NOT_FOUND));
        mapper.updateEntity(tarifa, request);
        TarifaSistema actualizada = tarifaSistemaRepository.save(tarifa);
        return mapper.toDTO(actualizada);
    }

    @Override
    public void eliminarTarifa(Long id) {
        if (!tarifaSistemaRepository.existsById(id)) {
            throw new TarifaSistemaException(ErrorCodeEnum.TARIFA_NOT_FOUND);
        }
        tarifaSistemaRepository.deleteById(id);
    }

    @Override
    public BigDecimal obtenerValorTarifaActual() {
        return tarifaSistemaRepository.findByActivaTrue()
                .map(TarifaSistema::getValorTarifa)
                .orElseThrow(() -> new TarifaSistemaException(ErrorCodeEnum.TARIFA_NOT_FOUND));
    }
}
