package com.sena.BogotaMetroApp.services.recarga;

import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.repository.transaccion.RecargaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RecargaServiceImpl implements IRecargarService{
    private final RecargaRepository recargaRepository;

    @Override
    public void guardarRecarga(Long recargaId, TarjetaVirtual tarjeta) {
        recargaRepository.findById(recargaId).ifPresent(recarga -> {
            recarga.setTarjetaVirtual(tarjeta);
            recargaRepository.save(recarga);
        });
    }
}
