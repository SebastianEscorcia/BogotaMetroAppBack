package com.sena.BogotaMetroApp.persistence.models.estacion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "estaciones")
@Getter
@Setter
public class Estacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estacion")
    private Long id;

    private String nombre;

    private BigDecimal latitud;

    private BigDecimal longitud;

    @Column(name = "es_accesible")
    private Boolean esAccesible;

    private String tipo;
}
