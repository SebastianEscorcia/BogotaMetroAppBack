package com.sena.BogotaMetroApp.persistence.models.sesionchat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "participantes_sesion",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_sesion", "id_usuario"}))
@Getter
@Setter
public class ParticipanteSesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sesion", nullable = false)
    @JsonBackReference
    private SesionChat sesionChat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_union")
    private LocalDateTime fechaUnion;

    @Column(name = "es_activo")
    private boolean activo = true;
}
