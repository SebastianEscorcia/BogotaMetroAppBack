package com.sena.BogotaMetroApp.services.qr;

import com.sena.BogotaMetroApp.persistence.models.Viaje;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.ValidarQrRequest;
import com.sena.BogotaMetroApp.presentation.dto.qr.ValidarQrResponse;
import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.services.exception.pago.PagoException;
import com.sena.BogotaMetroApp.services.exception.qr.QrException;
import com.sena.BogotaMetroApp.mapper.qr.QrMapper;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Transaccion;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.persistence.repository.transaccion.TransaccionRepository;
import com.sena.BogotaMetroApp.persistence.repository.qr.QrRepository;


import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import com.sena.BogotaMetroApp.utils.validators.QrValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;



@Service
@RequiredArgsConstructor
public class QrServiceImpl implements IQrService {

    private final QrMapper qrMapper;
    private final QrRepository qrRepository;
    private final QrValidator qrValidator;
    private final TransaccionRepository transaccionRepository;
    private final QrResponseBuilder qrResponseBuilder;


    @Override
    @Transactional
    public QrResponseDTO generarQr(QrRequestDTO request) {
        validarRequestConsistencia(request);
        Qr qr = qrMapper.toEntity(request);
        qrRepository.save(qr);
        return qrMapper.toDTO(qr);
    }

    @Override
    @Transactional
    public ValidarQrResponse validarQrEnTorniquete(ValidarQrRequest request) {
        Qr qr = qrRepository.findByContenidoQr(request.getContenidoQr())
                .orElseThrow(() -> new QrException(ErrorCodeEnum.QR_NOT_FOUND));

        qrValidator.validarQrParaTorniquete(qr);

        qr.setConsumido(true);
        qrRepository.save(qr);

        return qrResponseBuilder.buildSuccessResponse(qr);
    }

    @Override
    @Transactional
    public QrResponseDTO regenerarQrViaje(Long idPago) {
        Transaccion transaccion = transaccionRepository.findById(idPago)
                .orElseThrow(() -> new PagoException(ErrorCodeEnum.PAGO_NOT_FOUND));

        List<Qr> qrsAnteriores = qrRepository.findQrsByTransaccionOrderByFechaDesc(idPago);

        if (qrsAnteriores.isEmpty()) {
            throw new QrException(ErrorCodeEnum.QR_NO_PREVIOUS);
        }

        Qr qrAnterior = qrsAnteriores.get(0);
        qrValidator.validarParaRegeneracion(qrAnterior);

        QrRequestDTO request = crearRequestParaRegeneracion(transaccion, qrAnterior);
        return generarQr(request);
    }

    @Override
    @Transactional
    public Qr generarEntidadQrParaViaje(Pasajero pasajero, Viaje viaje, Transaccion transaccion) {

        Qr qr = new Qr();
        String contenidoUnico = qrMapper.generarContenidoQr(TipoQr.VIAJE, pasajero.getId(), viaje.getId());
        qr.setContenidoQr(contenidoUnico);
        qr.setTipo(TipoQr.VIAJE);
        qr.setFechaGeneracion(LocalDateTime.now());
        qr.setConsumido(false);
        qr.setUsuario(pasajero.getUsuario());
        qr.setViaje(viaje);
        qr.setTransaccion(transaccion);
        return qrRepository.save(qr);
    }


    private QrRequestDTO crearRequestParaRegeneracion(Transaccion transaccion, Qr qrAnterior) {
        QrRequestDTO request = new QrRequestDTO();
        request.setIdUsuario(transaccion.getUsuario().getId());
        request.setIdViaje(qrAnterior.getViaje().getId());
        request.setIdPago(transaccion.getId());
        request.setTipo(TipoQr.VIAJE);
        return request;
    }

    private void validarRequestConsistencia(QrRequestDTO request) {
        if (request.getTipo() == TipoQr.PAGO) {
            if (request.getIdPago() == null) {
                throw new QrException(ErrorCodeEnum.QR_PAGO_REQUIRED);
            }
            if (request.getIdViaje() != null) {
                throw new QrException(ErrorCodeEnum.QR_INVALID_COMBINATION);
            }
        }

        if (request.getTipo() == TipoQr.VIAJE) {
            if (request.getIdViaje() == null) {
                throw new QrException(ErrorCodeEnum.QR_VIAJE_REQUIRED);
            }
        }
    }


}
