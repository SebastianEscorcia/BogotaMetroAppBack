package com.sena.BogotaMetroApp.persistence.models.soportepasajero;

import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.persistence.models.soporte.Soporte;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "soportes_pasajeros")
@Getter
@Setter
public class SoportePasajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_soporte_pasajero")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_soporte", nullable = false)
    private Soporte soporte;

    @ManyToOne
    @JoinColumn(name = "id_pasajero", nullable = false)
    private Pasajero pasajero;

    @Column(name = "fecha_asignacion")
    private LocalDate fechaAsignacion;
}

