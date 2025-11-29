package com.sena.BogotaMetroApp.persistence.models;

import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "rutas")
@Getter
@Setter
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Long id;

    private Double distancia;

    @Column(name = "hora_inicio_ruta")
    private LocalTime horaInicioRuta;

    @Column(name = "fecha")
    private LocalDate fecha;

    // estaciones
    @ManyToOne
    @JoinColumn(name = "id_estacion_inicio")
    private Estacion estacionInicio;

    @ManyToOne
    @JoinColumn(name = "id_estacion_fin")
    private Estacion estacionFin;
}