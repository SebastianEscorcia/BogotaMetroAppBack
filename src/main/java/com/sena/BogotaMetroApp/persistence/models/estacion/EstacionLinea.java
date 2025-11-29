package com.sena.BogotaMetroApp.persistence.models.estacion;

import com.sena.BogotaMetroApp.persistence.models.Linea;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstacionLinea  {

    @EmbeddedId
    private EstacionLineaId id;

    @ManyToOne
    @MapsId("idLinea")
    @JoinColumn(name = "id_linea")
    private Linea linea;

    @ManyToOne
    @MapsId("idEstacion")
    @JoinColumn(name = "id_estacion")
    private Estacion estacion;

    private Integer orden;
}