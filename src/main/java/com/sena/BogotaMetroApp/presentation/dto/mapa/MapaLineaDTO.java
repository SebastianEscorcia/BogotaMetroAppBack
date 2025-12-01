package com.sena.BogotaMetroApp.presentation.dto.mapa;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MapaLineaDTO {
    private Long id;
    private String nombre;
    private String color;
    private List<MapaEstacionDTO> estaciones;
}
