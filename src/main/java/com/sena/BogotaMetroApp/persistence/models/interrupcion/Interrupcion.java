package com.sena.BogotaMetroApp.persistence.models.interrupcion;

import com.sena.BogotaMetroApp.persistence.models.Linea;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "interrupciones")
@Getter
@Setter
public class Interrupcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrupcion")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_estacion")
    private Estacion estacion;

    @ManyToOne
    @JoinColumn(name = "id_linea")
    private Linea linea;

    private String tipo;
    private String descripcion;

    private LocalDateTime inicio;
    private LocalDateTime fin;
}