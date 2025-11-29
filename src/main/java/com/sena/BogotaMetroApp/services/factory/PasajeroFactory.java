package com.sena.BogotaMetroApp.services.factory;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import org.springframework.stereotype.Component;

@Component
public class PasajeroFactory {

    public Pasajero crear(Usuario usuario) {
        Pasajero p = new Pasajero();
        p.setUsuario(usuario);
        return p;
    }
}
