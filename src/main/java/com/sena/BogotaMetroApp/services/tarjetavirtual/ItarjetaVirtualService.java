package com.sena.BogotaMetroApp.services.tarjetavirtual;

import java.math.BigDecimal;


public interface ItarjetaVirtualService {
    /**
     * Descuenta saldo de la tarjeta del usuario.
     * Lanza excepción si no tiene saldo o no tiene tarjeta.
     */
    void descontarSaldo(Long idUsuario, BigDecimal valorADescontar);
}
