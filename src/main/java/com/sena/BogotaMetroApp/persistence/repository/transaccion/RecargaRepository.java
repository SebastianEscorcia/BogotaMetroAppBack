package com.sena.BogotaMetroApp.persistence.repository.transaccion;

import com.sena.BogotaMetroApp.persistence.models.transaccion.Recarga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecargaRepository extends JpaRepository<Recarga,Long> {
    List<Recarga> findByPasarelaId(Long idPasarela);
    Optional<Recarga> findByReferenciaPasarela(String referenciaPasarela);
}
