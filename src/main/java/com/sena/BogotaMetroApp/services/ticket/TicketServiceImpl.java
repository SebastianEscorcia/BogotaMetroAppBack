package com.sena.BogotaMetroApp.services.ticket;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.mapper.qr.QrMapper;
import com.sena.BogotaMetroApp.persistence.models.Viaje;
import com.sena.BogotaMetroApp.persistence.models.transaccion.CompraTicket;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Transaccion;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViaje;
import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViajeId;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.persistence.repository.ViajeRepository;
import com.sena.BogotaMetroApp.persistence.repository.pasajero.PasajeroRepository;
import com.sena.BogotaMetroApp.persistence.repository.pasajeroviaje.PasajeroViajeRepository;
import com.sena.BogotaMetroApp.persistence.repository.transaccion.TransaccionRepository;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.viaje.ViajeResponseDTO;
import com.sena.BogotaMetroApp.services.exception.pasajero.PasajeroException;
import com.sena.BogotaMetroApp.services.exception.viaje.ViajeException;
import com.sena.BogotaMetroApp.services.qr.IQrService;
import com.sena.BogotaMetroApp.services.tarjetavirtual.ItarjetaVirtualService;
import com.sena.BogotaMetroApp.services.viaje.IViajeServices;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements IticketService {

    private final ItarjetaVirtualService tarjetaService;
    private final IViajeServices viajeService;
    private final IQrService qrService;

    private final PasajeroRepository pasajeroRepository;
    private final PasajeroViajeRepository pasajeroViajeRepository;
    private final ViajeRepository viajeRepository;
    private final TransaccionRepository transaccionRepository;
    private final QrMapper qrMapper;

    @Override
    @Transactional
    public QrResponseDTO comprarViaje(Long idUsuario, Long idViaje) {

        Pasajero pasajero = pasajeroRepository.findById(idUsuario)
                .orElseThrow(() -> new PasajeroException(ErrorCodeEnum.PASAJERO_NO_ENCONTRADO));

        Viaje viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new ViajeException(ErrorCodeEnum.VIAJE_NOT_FOUND));

        ViajeResponseDTO viajeDTO = viajeService.obtenerViaje(idViaje);

        // 2. Validar costo
        if (viajeDTO.getPresupuesto() == null || viajeDTO.getPresupuesto() <= 0) {
            throw new RuntimeException("El viaje no tiene un costo válido asignado");
        }
        BigDecimal costo = BigDecimal.valueOf(viajeDTO.getPresupuesto());

        CompraTicket compra = tarjetaService.descontarSaldo(idUsuario, costo);

        Qr qrGenerado = qrService.generarEntidadQrParaViaje(pasajero, viaje, compra);

        // Guardar Ticket de Pago
        PasajeroViaje ticket = new PasajeroViaje();
        ticket.setPasajero(pasajero);
        ticket.setViaje(viaje);
        ticket.setFechaRegistro(LocalDateTime.now());
        ticket.setQr(qrGenerado);

        PasajeroViaje ticketGuardado = pasajeroViajeRepository.save(ticket);

        // Guardamos la relacion con el ticket
        compra.setTicket(ticketGuardado);

        transaccionRepository.save(compra);

        return qrMapper.toDTO(qrGenerado);
    }
}
