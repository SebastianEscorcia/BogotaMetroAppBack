package com.sena.BogotaMetroApp.services.factory;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.persistence.repository.pasajero.PasajeroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasajeroFactory {

    private final PasajeroRepository pasajeroRepository;
    public Pasajero crear(Usuario usuario) {
        Pasajero p = new Pasajero();
        p.setUsuario(usuario);
        p.setId(usuario.getId());
        p = pasajeroRepository.save(p);
        return p;
    }
}
