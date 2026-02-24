package com.sena.BogotaMetroApp.presentation.dto.transaccion;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class PasarSaldoRequestDTO {

    @NotBlank(message = "El número de teléfono es requerido")
    @Pattern(
            regexp = "^(\\+57 )?3[0-9]{9}$",
            message = "El número debe iniciar en 3 y tener 10 dígitos, con o sin prefijo +57"
    )
    private String numTelefono;

    @NotNull(message = "El valor es requerido")
    @DecimalMin(value = "1000", message = "El valor mínimo es $1000")
    @DecimalMax(value = "10000000", message = "El valor máximo es $10.000.000")
    private BigDecimal valor;
}
