package com.sena.BogotaMetroApp.persistence.models.viajeruta;


import com.sena.BogotaMetroApp.persistence.models.Ruta;
import com.sena.BogotaMetroApp.persistence.models.Viaje;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "viajes_rutas")
@AllArgsConstructor
@NoArgsConstructor
public class ViajeRuta {
    @EmbeddedId
    private ViajeRutaId id = new ViajeRutaId();

    @ManyToOne
    @MapsId("idViaje")
    @JoinColumn(name = "id_viajeFK")
    private Viaje viaje;

    @ManyToOne
    @MapsId("idRuta")
    @JoinColumn(name = "id_rutaFK")
    private Ruta ruta;

}
