package com.sena.BogotaMetroApp.persistence.models.soporte;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "soportes")
@Getter
@Setter
public class Soporte {

    @Id
    @Column(name = "id_usuario")
    private Long id;

    @OneToOne(fetch =  FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimoAcceso;
}