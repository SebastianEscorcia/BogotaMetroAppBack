package com.sena.BogotaMetroApp.services.tarjetavirtual;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.repository.TarjetaVirtualRepository;
import com.sena.BogotaMetroApp.services.exception.pago.PagoException;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import com.sena.BogotaMetroApp.utils.enums.EstadoTarjetaEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Slf4j
public class TarjetaVirtualImpl implements ItarjetaVirtualService {

    private final TarjetaVirtualRepository tarjetaVirtualRepository;

    @Override
    @Transactional
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

    /**
     * Recarga el saldo de la tarjeta virtual.
     * Usamos Propagation.REQUIRES_NEW porque este método puede ser llamado
     * desde un @TransactionalEventListener (AFTER_COMMIT), donde ya no hay
     * una transacción activa. REQUIRES_NEW garantiza que se cree una nueva
     * transacción independiente que hará su propio commit.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TarjetaVirtual recargarSaldo(Long idUsuario, BigDecimal valorARecargar) {
        log.info("Iniciando recarga de saldo para usuario {} por monto {}", idUsuario, valorARecargar);

        TarjetaVirtual tarjeta = tarjetaVirtualRepository.findByPasajeroUsuarioId(idUsuario)
                .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_DONT_CARD_ACTIVE));

        BigDecimal saldoAnterior = tarjeta.getSaldo();
        tarjeta.setSaldo(saldoAnterior.add(valorARecargar));

        TarjetaVirtual tarjetaActualizada = tarjetaVirtualRepository.save(tarjeta);
        log.info("Saldo actualizado: {} -> {}", saldoAnterior, tarjetaActualizada.getSaldo());

        return tarjetaActualizada;
    }
}
