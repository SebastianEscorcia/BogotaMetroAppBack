package com.sena.BogotaMetroApp.services.recarga;

import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;


public interface IRecargarService {
        void guardarRecarga(Long recargaId, TarjetaVirtual tarjeta);
}
