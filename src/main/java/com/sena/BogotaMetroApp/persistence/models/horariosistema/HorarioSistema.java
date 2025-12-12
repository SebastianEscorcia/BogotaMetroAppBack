package com.sena.BogotaMetroApp.persistence.models.horariosistema;

import com.sena.BogotaMetroApp.persistence.models.AuditableEntity;
import com.sena.BogotaMetroApp.utils.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalTime;
@Getter
@Setter
@Entity
@Table(name = "horarios_sistema")

public class HorarioSistema extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Long id;

    @Enumerated(EnumType.STRING)
    private DiaSemana dia;

    private LocalTime horaInicio;
    private LocalTime horaFin;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    public Boolean getActivo() {
        return this.activo;
    }
}
