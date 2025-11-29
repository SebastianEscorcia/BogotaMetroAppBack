package com.sena.BogotaMetroApp.persistence.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lineas")
@Getter
@Setter
public class Linea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_linea")
    private Long id;

    private String nombre;

    private String color;

    @Column(name = "frecuencia_minutos")
    private Integer frecuenciaMinutos;
}
