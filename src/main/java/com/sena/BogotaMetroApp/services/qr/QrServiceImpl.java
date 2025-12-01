package com.sena.BogotaMetroApp.services.qr;

import com.sena.BogotaMetroApp.persistence.models.Viaje;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViaje;
import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViajeId;
import com.sena.BogotaMetroApp.persistence.repository.pasajero.PasajeroRepository;
import com.sena.BogotaMetroApp.persistence.repository.pasajeroviaje.PasajeroViajeRepository;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.ValidarQrRequest;
import com.sena.BogotaMetroApp.presentation.dto.qr.ValidarQrResponse;
import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.services.exception.pago.PagoException;
import com.sena.BogotaMetroApp.services.exception.pasajero.PasajeroException;
import com.sena.BogotaMetroApp.services.exception.qr.QrException;
import com.sena.BogotaMetroApp.mapper.qr.QrMapper;
import com.sena.BogotaMetroApp.persistence.models.pago.Pago;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.persistence.repository.ViajeRepository;
import com.sena.BogotaMetroApp.persistence.repository.pago.PagoRepository;
import com.sena.BogotaMetroApp.persistence.repository.qr.QrRepository;

import com.sena.BogotaMetroApp.services.exception.viaje.ViajeException;
import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import com.sena.BogotaMetroApp.utils.validators.QrValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class QrServiceImpl implements IQrService {

    private final QrMapper qrMapper;
    private final QrRepository qrRepository;
    private final QrValidator qrValidator;
    private final PagoRepository pagoRepository;
    private final QrResponseBuilder qrResponseBuilder;
    private final ViajeRepository viajeRepository;
    private final PasajeroRepository pasajeroRepository;
    private final PasajeroViajeRepository pasajeroViajeRepository;


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
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new PagoException(ErrorCodeEnum.PAGO_NOT_FOUND));

        List<Qr> qrsAnteriores = qrRepository.findQrsByPagoOrderByFechaDesc(idPago);

        if (qrsAnteriores.isEmpty()) {
            throw new QrException(ErrorCodeEnum.QR_NO_PREVIOUS);
        }

        Qr qrAnterior = qrsAnteriores.get(0);
        qrValidator.validarParaRegeneracion(qrAnterior);

        QrRequestDTO request = crearRequestParaRegeneracion(pago, qrAnterior);
        return generarQr(request);
    }

    @Override
    public QrResponseDTO generarQrParaViaje(Long idUsuario, Long idViaje) {
        // 1. Recuperar las entidades necesarias
        // Nota: idUsuario es igual a idPasajero por el @MapsId
        Pasajero pasajero = pasajeroRepository.findById(idUsuario)
                .orElseThrow(() -> new PasajeroException(ErrorCodeEnum.PASAJERO_NO_ENCONTRADO));

        Viaje viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new ViajeException(ErrorCodeEnum.VIAJE_NOT_FOUND));

        // 2. Crear y Guardar primero el QR
        Qr qr = new Qr();
        // Generamos un string único. Puedes usar UUID o una lógica propia encriptada
        String contenidoUnico = "METRO-" + UUID.randomUUID().toString() + "-V" + idViaje;

        qr.setContenidoQr(contenidoUnico);
        qr.setTipo(TipoQr.VIAJE);
        qr.setFechaGeneracion(LocalDateTime.now());
        qr.setConsumido(false);
        qr.setUsuario(pasajero.getUsuario());
        qr.setViaje(viaje); // Relación opcional en QR, pero útil para trazabilidad rápida

        Qr qrGuardado = qrRepository.save(qr);

        // 3. Crear el Ticket (PasajeroViaje) y asociar el QR
        PasajeroViaje ticket = new PasajeroViaje();

        // Configurar ID Compuesto (Obligatorio por @EmbeddedId)
        PasajeroViajeId idCompuesto = new PasajeroViajeId();
        idCompuesto.setPasajeroId(pasajero.getId());
        idCompuesto.setViajeId(viaje.getId());

        ticket.setId(idCompuesto);
        ticket.setPasajero(pasajero);
        ticket.setViaje(viaje);
        ticket.setFechaRegistro(LocalDateTime.now());
        ticket.setQr(qrGuardado);

        pasajeroViajeRepository.save(ticket);

        // 4. Retornar DTO
        return qrMapper.toDTO(qrGuardado);
    }



    private QrRequestDTO crearRequestParaRegeneracion(Pago pago, Qr qrAnterior) {
        QrRequestDTO request = new QrRequestDTO();
        request.setIdUsuario(pago.getUsuario().getId());
        request.setIdViaje(qrAnterior.getViaje().getId());
        request.setIdPago(pago.getId());
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
            if (request.getIdPago() != null) {
                throw new QrException(ErrorCodeEnum.QR_INVALID_COMBINATION);
            }
        }
    }


}
