package com.sena.BogotaMetroApp.services.factory;

import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.utils.enums.EstadoTarjetaEnum;
import com.sena.BogotaMetroApp.utils.logic.TarjetaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TarjetaVirtualFactory {

    public TarjetaVirtual crearTarjetaVirtual(Pasajero pasajero) {
        TarjetaVirtual tarjetaVirtual = new TarjetaVirtual();
        tarjetaVirtual.setNumeroTarjeta(TarjetaUtil.generarNumeroTarjeta());
        tarjetaVirtual.setSaldo(BigDecimal.ZERO);
        tarjetaVirtual.setEstado(EstadoTarjetaEnum.ACTIVA);
        tarjetaVirtual.setPasajero(pasajero);
        return tarjetaVirtual;

    }
}
