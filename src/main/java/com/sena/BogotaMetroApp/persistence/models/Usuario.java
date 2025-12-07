package com.sena.BogotaMetroApp.persistence.models;

import com.sena.BogotaMetroApp.persistence.models.operador.Operador;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.persistence.models.soporte.Soporte;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(nullable = false)
    private String clave;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Role rol;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private DatosPersonales datosPersonales;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Pasajero pasajero;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Operador operador;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Soporte soporte;
}
