package com.sena.BogotaMetroApp.persistence.repository.viajeruta;

import com.sena.BogotaMetroApp.persistence.models.viajeruta.ViajeRuta;
import com.sena.BogotaMetroApp.persistence.models.viajeruta.ViajeRutaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViajeRutaRepository extends JpaRepository<ViajeRuta, ViajeRutaId> {
}
