package com.sena.BogotaMetroApp.presentation.dto.pasajero;

import com.sena.BogotaMetroApp.utils.enums.TipoDocumentoEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PasajeroResponseDTO {

    private Long id;
    private Long idUsuario;
    private Long idTarjetaVirtual;
    private String correo;
    private String nombreCompleto;
    private String telefono;
    private TipoDocumentoEnum tipoDocumento;
    private String numDocumento;

    private BigDecimal saldo;
}
