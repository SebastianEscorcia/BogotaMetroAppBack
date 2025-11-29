package com.sena.BogotaMetroApp.persistence.models.puntointeres;

import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "puntos_interes")
@Getter @Setter
public class PuntoInteres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_poi")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_estacion", nullable = false)
    private Estacion estacion;

    private String nombre;

    private String categoria;

    private BigDecimal latitud;

    private BigDecimal longitud;
}