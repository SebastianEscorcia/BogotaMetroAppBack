package com.sena.BogotaMetroApp.services.tarjetavirtual;

import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;

import java.math.BigDecimal;


public interface ItarjetaVirtualService {
    /**
     * Descuenta saldo de la tarjeta del usuario.
     * Lanza excepción si no tiene saldo o no tiene tarjeta.
     */
    void descontarSaldo(Long idUsuario, BigDecimal valorADescontar);
    /**
     * Recarga saldo a la tarjeta del usuario.
     */
    TarjetaVirtual recargarSaldo(Long idUsuario, BigDecimal valorARecargar);
}
