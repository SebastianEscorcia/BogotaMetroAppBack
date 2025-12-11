package com.sena.BogotaMetroApp.services.tarjetavirtual;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.repository.TarjetaVirtualRepository;
import com.sena.BogotaMetroApp.services.exception.pago.PagoException;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import com.sena.BogotaMetroApp.utils.enums.EstadoTarjetaEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class TarjetaVirtualImpl implements ItarjetaVirtualService {

    private final TarjetaVirtualRepository tarjetaVirtualRepository;

    @Override
    public void descontarSaldo(Long idUsuario, BigDecimal valorADescontar) {
        TarjetaVirtual tarjeta = tarjetaVirtualRepository.findByPasajeroUsuarioId(idUsuario)
                .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_DONT_CARD_ACTIVE));

        if (tarjeta.getEstado() != EstadoTarjetaEnum.ACTIVA) {
            throw new PagoException(ErrorCodeEnum.USUARIO_DONT_CARD_ACTIVE);
        }

        if (tarjeta.getSaldo().compareTo(valorADescontar) < 0) {
            throw new PagoException(ErrorCodeEnum.SALDO_INSUFICIENTE);
        }

        tarjeta.setSaldo(tarjeta.getSaldo().subtract(valorADescontar));
        tarjetaVirtualRepository.save(tarjeta);
    }
}
