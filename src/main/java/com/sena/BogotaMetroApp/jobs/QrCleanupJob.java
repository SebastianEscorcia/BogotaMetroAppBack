package com.sena.BogotaMetroApp.jobs;

import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.persistence.models.qr.QrNoUsado;
import com.sena.BogotaMetroApp.persistence.repository.qr.QrNoUsadoRepository;
import com.sena.BogotaMetroApp.persistence.repository.qr.QrRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class QrCleanupJob {

    private final QrRepository qrRepository;
    private final QrNoUsadoRepository qrNoUsadoRepository;

    /**
     * Limpia QRs expirados cada 5 minutos.
     *
     * NOTA: No es necesario invalidar Redis porque:
     * 1. El TTL de Redis ya eliminó automáticamente el caché cuando expiró
     * 2. Si el usuario pide un nuevo QR, el servicio ya maneja la limpieza
     *
     * Este job solo sirve para limpiar la BD de QRs que expiraron
     * y el usuario nunca volvió a solicitar uno nuevo.
     */
    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void limpiarQrsExpirados() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Qr> qrsExpirados = qrRepository.findQrsExpirados(ahora);

        if (qrsExpirados.isEmpty()) {
            return; // Nada que limpiar
        }

        log.info("Limpiando {} QRs expirados", qrsExpirados.size());

        int movidos = 0;
        int eliminados = 0;

        for (Qr qr : qrsExpirados) {
            try {
                // Solo mover a histórico si NO fue consumido
                if (!qr.getConsumido()) {
                    QrNoUsado qrNoUsado = new QrNoUsado();
                    qrNoUsado.setTipo(qr.getTipo());
                    qrNoUsado.setContenidoQr(qr.getContenidoQr());
                    qrNoUsado.setFechaGeneracion(qr.getFechaGeneracion());
                    qrNoUsado.setFechaExpiracion(qr.getFechaExpiracion());
                    qrNoUsado.setFechaMovimiento(ahora);
                    qrNoUsado.setUsuario(qr.getUsuario());
                    qrNoUsadoRepository.save(qrNoUsado);
                    movidos++;
                }

                qrRepository.delete(qr);
                eliminados++;

            } catch (Exception e) {
                log.error("Error limpiando QR id={}: {}", qr.getId(), e.getMessage());
            }
        }

        log.info("Limpieza completada: {} movidos a histórico, {} eliminados", movidos, eliminados);
    }
}