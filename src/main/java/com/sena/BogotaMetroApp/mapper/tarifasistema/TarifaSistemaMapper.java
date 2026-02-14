package com.sena.BogotaMetroApp.mapper.tarifasistema;


import com.sena.BogotaMetroApp.persistence.models.tarifasistema.TarifaSistema;
import com.sena.BogotaMetroApp.presentation.dto.tarifasistema.TarifaSistemaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.tarifasistema.TarifaSistemaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class TarifaSistemaMapper {

    public TarifaSistema toEntity(TarifaSistemaRequestDTO dto) {
        TarifaSistema tarifa = new TarifaSistema();
        tarifa.setValorTarifa(dto.getValorTarifa());
        //tarifa.setDescripcion(dto.getDescripcion());
        tarifa.setActiva(dto.getActiva() != null ? dto.getActiva() : true);
        return tarifa;
    }

    public TarifaSistemaResponseDTO toDTO(TarifaSistema tarifa) {
        TarifaSistemaResponseDTO dto = new TarifaSistemaResponseDTO();
        dto.setId(tarifa.getId());
        dto.setValorTarifa(tarifa.getValorTarifa());
        dto.setDescripcion(tarifa.getDescripcion());
        dto.setActiva(tarifa.getActiva());
        dto.setFechaCreacion(tarifa.getCreatedAt());
        dto.setFechaActualizacion(tarifa.getUpdatedAt());
        return dto;
    }

    public void updateEntity(TarifaSistema tarifa, TarifaSistemaRequestDTO dto) {
        if (dto.getValorTarifa() != null) {
            tarifa.setValorTarifa(dto.getValorTarifa());
        }
        if (dto.getDescripcion() != null) {
            tarifa.setDescripcion(dto.getDescripcion());
        }
        if (dto.getActiva() != null) {
            tarifa.setActiva(dto.getActiva());
        }
    }
}