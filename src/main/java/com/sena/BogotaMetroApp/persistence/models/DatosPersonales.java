package com.sena.BogotaMetroApp.persistence.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "datos_personales")
@Getter @Setter
public class DatosPersonales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_datop")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    private String nombreCompleto;
    private String telefono;
    private String tipoDocumento;
    private String numDocumento;
    private Date fechaNacimiento;
    private String direccion;
}