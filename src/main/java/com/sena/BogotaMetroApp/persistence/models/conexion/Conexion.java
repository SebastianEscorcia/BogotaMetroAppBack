package com.sena.BogotaMetroApp.persistence.models.conexion;

import com.sena.BogotaMetroApp.persistence.models.AuditableEntity;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "conexiones")
@Getter
@Setter
public class Conexion extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conexion")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_origen", nullable = false)
    private Estacion origen;

    @ManyToOne
    @JoinColumn(name = "id_destino", nullable = false)
    private Estacion destino;

    @Column(name = "distancia_metros")
    private Integer distanciaMetros;

    @Column(name = "tiempo_minutos")
    private Integer tiempoMinutos;
}

