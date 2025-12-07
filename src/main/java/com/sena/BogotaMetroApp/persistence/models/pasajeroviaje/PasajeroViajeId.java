package com.sena.BogotaMetroApp.persistence.models.pasajeroviaje;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasajeroViajeId implements Serializable {

    @Column(name = "id_pasajero")
    private Long pasajeroId;

    @Column(name = "id_viaje")
    private Long viajeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasajeroViajeId that = (PasajeroViajeId) o;
        return Objects.equals(pasajeroId, that.pasajeroId)
                && Objects.equals(viajeId, that.viajeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pasajeroId, viajeId);
    }
}