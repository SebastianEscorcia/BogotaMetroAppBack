package com.sena.BogotaMetroApp.persistence.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "viajes")
@Getter
@Setter
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viaje")
    private Long id;

    @Column(name = "nombre_viaje", nullable = false)
    private String nombreViaje;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin", nullable = false)
    private LocalDateTime fechaHoraFin;

    private String descripcion;

    private Double presupuesto;

    @Column(name = "estado_viaje")
    private Integer estadoViaje;

    // --- RELACIÓN MUCHOS A MUCHOS (viajes <-> rutas)
    @ManyToMany
    @JoinTable(
            name = "viajes_rutas",
            joinColumns = @JoinColumn(name = "id_viajeFK"),
            inverseJoinColumns = @JoinColumn(name = "id_rutaFK")
    )
    private List<Ruta> rutas = new ArrayList<>();
}