package com.sena.BogotaMetroApp.persistence.models.interrupcion;

import com.sena.BogotaMetroApp.persistence.models.Linea;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import com.sena.BogotaMetroApp.utils.enums.EstadoInterrupcionEnum;
import com.sena.BogotaMetroApp.utils.enums.InterruptionTypeEnum;
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

    @Enumerated(EnumType.STRING)
    private InterruptionTypeEnum tipo;

    private String descripcion;

    private LocalDateTime inicio;
    private LocalDateTime fin;

    @Enumerated(EnumType.STRING)
    private EstadoInterrupcionEnum estado;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}