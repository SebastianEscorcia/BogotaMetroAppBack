package com.sena.BogotaMetroApp.persistence.repository.estacion;

import com.sena.BogotaMetroApp.persistence.models.estacion.EstacionLinea;
import com.sena.BogotaMetroApp.persistence.models.estacion.EstacionLineaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EstacionLineaRepository extends JpaRepository<EstacionLinea, EstacionLineaId> {

    List<EstacionLinea> findByLineaId(Long idLinea);

}
