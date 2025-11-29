package com.sena.BogotaMetroApp.persistence.models.pasajero;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pasajeros")
@Getter
@Setter
public class Pasajero {

    @Id
    @Column(name = "id_usuario")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}