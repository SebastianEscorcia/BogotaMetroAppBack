package com.sena.BogotaMetroApp.persistence.repository;

import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TarjetaVirtualRepository extends JpaRepository<TarjetaVirtual, Long> {
    Optional<TarjetaVirtual> findByPasajeroUsuarioId(Long idUsuario);
    Optional<TarjetaVirtual> findTarjetaVirtualByPasajeroUsuarioDatosPersonalesTelefono(String telefono);
}
