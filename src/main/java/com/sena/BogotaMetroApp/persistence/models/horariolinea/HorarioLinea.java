package com.sena.BogotaMetroApp.persistence.models.horariolinea;

import com.sena.BogotaMetroApp.persistence.models.Linea;
import com.sena.BogotaMetroApp.utils.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "horarios_lineas")
@Getter
@Setter
public class HorarioLinea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_linea")
    private Linea linea;

    @Enumerated(EnumType.STRING)
    private DiaSemana dia;

    private LocalTime horaInicio;
    private LocalTime horaFin;
}
