package com.sena.BogotaMetroApp.presentation.dto.usuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegistroUsuarioBaseDTO {

    // Datos de Usuario
    @NotNull
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correo;
    @NotNull
    private String clave;

    // Datos Personales
    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;
    private String telefono;
    private String tipoDocumento;
    private String numDocumento;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private String direccion;
}
