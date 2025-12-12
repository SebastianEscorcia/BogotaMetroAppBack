package com.sena.BogotaMetroApp.services.torniquete;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.persistence.models.transaccion.CobroPasaje;
import com.sena.BogotaMetroApp.persistence.repository.transaccion.TransaccionRepository;
import com.sena.BogotaMetroApp.presentation.dto.estacion.EstacionResponseDTO;
import com.sena.BogotaMetroApp.services.estacion.IEstacionServices;
import com.sena.BogotaMetroApp.services.exception.pago.PagoException;
import com.sena.BogotaMetroApp.services.horariosistema.IHorarioSistemaService;
import com.sena.BogotaMetroApp.services.qr.IQrService;
import com.sena.BogotaMetroApp.services.tarifasistema.ITarifaSistemaService;
import com.sena.BogotaMetroApp.services.tarjetavirtual.ItarjetaVirtualService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TorniqueteServiceImpl implements ITorniqueteService{

    private final ItarjetaVirtualService tarjetaService;
    private final TransaccionRepository transaccionRepository;
    private final IEstacionServices estacionServices;
    private final ITarifaSistemaService tarifaService;

    private final IHorarioSistemaService horarioService;

    private final IQrService qrService;

    //private static final BigDecimal TARIFA_METRO = new BigDecimal("2950");

    @Override
    public void procesarIngreso(String contenidoQr, Long idEstacion) {
        Qr qr = qrService.validarYObtenerPorContenido(contenidoQr);
        //  BigDecimal tarifaActual = tarifaService.obtenerValorTarifaActual();
        if (!horarioService.validarHorarioActual()) {
            throw new RuntimeException("Ingreso no permitido: Fuera del horario de operación del Metro");
        }

        Usuario usuario = qr.getUsuario();

        TarjetaVirtual tarjeta = usuario.getPasajero().getTarjetaVirtual();

        tarjetaService.descontarSaldo(usuario.getId(), tarifaService.obtenerValorTarifaActual());

        if(tarjeta.getSaldo().compareTo(tarifaService.obtenerValorTarifaActual()) < 0){
            throw new PagoException(ErrorCodeEnum.SALDO_INSUFICIENTE);
        }
        EstacionResponseDTO estacion =  estacionServices.obtener(idEstacion);

        CobroPasaje cobro = new CobroPasaje();
        cobro.setUsuario(usuario);
        cobro.setTarjetaVirtual(tarjeta);
        cobro.setEstacionId(idEstacion);
        cobro.setValor(tarifaService.obtenerValorTarifaActual());
        cobro.setFecha(LocalDateTime.now());
        cobro.setDescripcion("Ingreso al sistema de metro" + " en la estación " + estacion.getNombre());
        cobro.setEstacionId(idEstacion);

        transaccionRepository.save(cobro);

        qrService.consumirQr(qr);

    }


}
