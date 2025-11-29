package com.sena.BogotaMetroApp.persistence.models.viajeruta;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class ViajeRutaId implements Serializable {

    @Column(name = "id_viajeFK")
    private Long idViaje;

    @Column(name = "id_rutaFK")
    private Long idRuta;
}
