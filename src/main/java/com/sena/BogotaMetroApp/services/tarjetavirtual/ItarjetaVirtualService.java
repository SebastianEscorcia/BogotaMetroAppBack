package com.sena.BogotaMetroApp.services.tarjetavirtual;

import com.sena.BogotaMetroApp.persistence.models.transaccion.CompraTicket;
import java.math.BigDecimal;


public interface ItarjetaVirtualService {
    CompraTicket descontarSaldo(Long idUsuario, BigDecimal valorADescontar);
}
