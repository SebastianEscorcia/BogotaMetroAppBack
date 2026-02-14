package com.sena.BogotaMetroApp.persistence.repository.transaccion;

import com.sena.BogotaMetroApp.persistence.models.transaccion.Recarga;
import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecargaRepository extends JpaRepository<Recarga,Long> {
    List<Recarga> findRecargaByMedioDePago (MedioPagoEnum medioDePago);
}
