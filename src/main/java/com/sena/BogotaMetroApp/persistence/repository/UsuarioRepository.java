package com.sena.BogotaMetroApp.persistence.repository;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario>  findByCorreo(String correo);
    Optional<Usuario> findByDatosPersonales_NumDocumento(String numDocumento);
    Optional<Usuario> findByDatosPersonales_Telefono(String telefono);
    boolean existsByCorreo(String correo);
}
