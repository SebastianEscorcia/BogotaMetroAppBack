package com.sena.BogotaMetroApp.persistence.models.sesionchat;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sena.BogotaMetroApp.utils.enums.EstadoSesionEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sesiones_chat")
@Getter
@Setter
public class SesionChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "sesionChat", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ParticipanteSesion> participantes;

    @Enumerated(EnumType.STRING)
    private EstadoSesionEnum estado;

    @Column(name = "fecha_ultima_actividad")
    private LocalDateTime fechaUltimaActividad;

    private LocalDateTime fechaAsignacion;

    private LocalDateTime fechaCierre;

}
