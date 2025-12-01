package com.sena.BogotaMetroApp.services.tarjetavirtual;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.repository.TarjetaVirtualRepository;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TarjetaVirtualImpl implements ItarjetaVirtualService {

    private final TarjetaVirtualRepository tarjetaVirtualRepository;

    @Override
    public void descontarSaldo(Long idUsuario, BigDecimal valorADescontar) {
        // 1. Buscar la tarjeta del usuario
        TarjetaVirtual tarjeta = tarjetaVirtualRepository.findByPasajeroUsuarioId(idUsuario)
                .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_DONT_CARD_ACTIVE));

        // 2. Validar si tiene saldo suficiente
        // compareTo devuelve -1 si es menor, 0 si es igual, 1 si es mayor
        if (tarjeta.getSaldo().compareTo(valorADescontar) < 0) {
            throw new RuntimeException("Saldo insuficiente. Saldo actual: " + tarjeta.getSaldo());
        }

        // 3. Realizar el descuento
        tarjeta.setSaldo(tarjeta.getSaldo().subtract(valorADescontar));

        // 4. Guardar cambios
        tarjetaVirtualRepository.save(tarjeta);
    }
}
