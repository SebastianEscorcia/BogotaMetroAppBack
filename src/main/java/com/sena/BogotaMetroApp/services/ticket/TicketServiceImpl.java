package com.sena.BogotaMetroApp.services.ticket;

import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.viaje.ViajeResponseDTO;
import com.sena.BogotaMetroApp.services.qr.IQrService;
import com.sena.BogotaMetroApp.services.tarjetavirtual.ItarjetaVirtualService;
import com.sena.BogotaMetroApp.services.viaje.IViajeServices;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements IticketService {

    private final ItarjetaVirtualService tarjetaService;
    private final IViajeServices viajeService;
    private final IQrService qrService;


    @Override
    @Transactional
    public QrResponseDTO comprarViaje(Long idUsuario, Long idViaje) {
        ViajeResponseDTO viajeDTO = viajeService.obtenerViaje(idViaje);

        // 2. Validar costo
        if (viajeDTO.getPresupuesto() == null || viajeDTO.getPresupuesto() <= 0) {
            throw new RuntimeException("El viaje no tiene un costo válido asignado");
        }
        BigDecimal costo = BigDecimal.valueOf(viajeDTO.getPresupuesto());

        tarjetaService.descontarSaldo(idUsuario, costo);

        return qrService.generarQrParaViaje(idUsuario, idViaje);
    }
}
