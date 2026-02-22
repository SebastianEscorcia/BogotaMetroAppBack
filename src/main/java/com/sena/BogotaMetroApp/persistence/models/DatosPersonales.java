package com.sena.BogotaMetroApp.persistence.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sena.BogotaMetroApp.presentation.dto.common.IDatosPersonalesUpdate;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroUpdateDTO;
import com.sena.BogotaMetroApp.utils.enums.TipoDocumentoEnum;
import jakarta.persistence.*;
import lombok.*;

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


    public void actualizarDatos(IDatosPersonalesUpdate nuevosDatos) {
        this.nombreCompleto = nuevosDatos.getNombreCompleto();
        this.telefono = nuevosDatos.getTelefono();
        this.fechaNacimiento = nuevosDatos.getFechaNacimiento();
        this.direccion = nuevosDatos.getDireccion();
    }
    public void actualizarPasajero(
            PasajeroUpdateDTO dto
    ) {

        if (dto.nombreCompleto() != null && !dto.nombreCompleto().isBlank()) {
            this.nombreCompleto = dto.nombreCompleto();
        }

        if (dto.fechaNacimiento() != null) {
            this.fechaNacimiento = dto.fechaNacimiento();
        }

        if (dto.direccion() != null && !dto.direccion().isBlank()) {
            this.direccion = dto.direccion();
        }
    }
}