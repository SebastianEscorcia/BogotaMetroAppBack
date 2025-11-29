package com.sena.BogotaMetroApp.persistence.models.operador;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "operadores")
@Getter
@Setter
public class Operador {

    @Id
    @Column(name = "id_usuario")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
