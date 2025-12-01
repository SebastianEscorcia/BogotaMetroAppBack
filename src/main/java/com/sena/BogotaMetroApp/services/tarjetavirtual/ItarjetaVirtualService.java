package com.sena.BogotaMetroApp.services.tarjetavirtual;

import java.math.BigDecimal;


public interface ItarjetaVirtualService {
    void descontarSaldo(Long idUsuario, BigDecimal valorADescontar);
}
