package com.sena.BogotaMetroApp.services.network;

import com.sena.BogotaMetroApp.persistence.models.Linea;
import com.sena.BogotaMetroApp.persistence.models.estacion.EstacionLinea;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionLineaRepository;
import com.sena.BogotaMetroApp.persistence.repository.linea.LineaRepository;
import com.sena.BogotaMetroApp.presentation.dto.mapa.MapaEstacionDTO;
import com.sena.BogotaMetroApp.presentation.dto.mapa.MapaLineaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NetworkServiceImpl implements INetworkService {

    private final LineaRepository lineaRepository;
    private final EstacionLineaRepository estacionLineaRepository;

    @Override
    public List<MapaLineaDTO> obtenerMapaCompleto() {
        List<Linea> lineas = lineaRepository.findAll();
        List<MapaLineaDTO> mapa = new ArrayList<>();
        for (Linea linea : lineas) {
            MapaLineaDTO dto = new MapaLineaDTO();
            dto.setId(linea.getId());
            dto.setNombre(linea.getNombre());
            dto.setColor(linea.getColor());

            List<EstacionLinea> relaciones = estacionLineaRepository.findByLineaId(linea.getId());

            List<MapaEstacionDTO> estacionesDTO = relaciones.stream()
                    .sorted(Comparator.comparing(EstacionLinea::getOrden))
                    .map(rel -> new MapaEstacionDTO(
                            rel.getEstacion().getId(),
                            rel.getEstacion().getNombre(),
                            rel.getEstacion().getLatitud(),
                            rel.getEstacion().getLongitud(),
                            rel.getOrden()
                    ))
                    .collect(Collectors.toList());

            dto.setEstaciones(estacionesDTO);
            mapa.add(dto);
        }
        return mapa;


    }
}
