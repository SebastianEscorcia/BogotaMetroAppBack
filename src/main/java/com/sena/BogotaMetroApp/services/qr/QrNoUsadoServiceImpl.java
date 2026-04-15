package com.sena.BogotaMetroApp.services.qr;

import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.persistence.models.qr.QrNoUsado;
import com.sena.BogotaMetroApp.persistence.repository.qr.QrNoUsadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QrNoUsadoServiceImpl implements IQrNoUsadoService {

    private final QrNoUsadoRepository qrNoUsadoRepository;

    @Override
    public void moverQrNoUsadoAndExpirado(Qr qr) {
        QrNoUsado noUsado = new QrNoUsado();
        noUsado.setUsuario(qr.getUsuario());
        noUsado.setContenidoQr(qr.getContenidoQr());
        noUsado.setFechaGeneracion(qr.getFechaGeneracion());
        noUsado.setFechaExpiracion(qr.getFechaExpiracion());
        noUsado.setFechaMovimiento(LocalDateTime.now());
        noUsado.setTipo(qr.getTipo());
        qrNoUsadoRepository.save(noUsado);

    }
}
