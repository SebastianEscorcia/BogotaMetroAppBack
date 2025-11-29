
package com.sena.BogotaMetroApp.persistence.models.pasarela;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pasarelas")
@Getter
@Setter
public class Pasarela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pasarela")
    private Long id;

    @Column(name = "nombre", length = 20, nullable = false)
    private String nombre;

    @Column(name = "codigo", nullable = false)
    private Integer codigo;

    @Column(name = "pais", length = 20, nullable = false)
    private String pais;
}

