package com.sena.BogotaMetroApp.persistence.repository;

import com.sena.BogotaMetroApp.persistence.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNombre(String nombre);

}
