package com.sena.BogotaMetroApp.persistence.models.estacion;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EstacionLineaId implements Serializable {

    @Column(name = "id_linea")
    private Long idLinea;

    @Column(name = "id_estacion")
    private Long idEstacion;
}