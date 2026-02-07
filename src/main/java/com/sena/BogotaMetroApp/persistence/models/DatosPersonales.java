package com.sena.BogotaMetroApp.persistence.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sena.BogotaMetroApp.utils.enums.TipoDocumentoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "datos_personales")
@Getter
@Setter
public class DatosPersonales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_datop")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    @JsonBackReference
    private Usuario usuario;

    private String nombreCompleto;
    private String telefono;
    @Enumerated(EnumType.STRING)
    private TipoDocumentoEnum tipoDocumento;
    private String numDocumento;
    private LocalDate fechaNacimiento;
    private String direccion;
}